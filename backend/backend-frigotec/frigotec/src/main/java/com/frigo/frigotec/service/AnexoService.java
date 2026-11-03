package com.frigo.frigotec.service;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.AnexoDto;
import com.frigo.frigotec.entity.Anexo;
import com.frigo.frigotec.entity.CategoriaAnexo;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.AcessoNegadoException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.exception.ConflitoException;
import com.frigo.frigotec.exception.RegraDeNegocioException;
import com.frigo.frigotec.repository.AnexoRepository;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import com.frigo.frigotec.util.StatusChamadoEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Envio de arquivos (SDD RN-CHM-03 e RN-EUS-05). Fotos e vídeo do defeito ficam no chamado; nota fiscal e comprovantes
 * ficam na unidade do cliente e só valem para a marca própria. O tipo é conferido pelo conteúdo, não só pelo nome.
 */
@Service
public class AnexoService {

    private static final Set<String> TIPOS_CHAMADO = Set.of("image/jpeg", "image/png", "video/mp4");
    private static final Set<String> TIPOS_DOCUMENTO = Set.of("application/pdf", "image/jpeg", "image/png");

    private final AnexoRepository anexoRepository;
    private final ChamadoRepository chamadoRepository;
    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArmazenamentoService armazenamento;
    private final long tamanhoMaximo;
    private final int maxFotosPorChamado;
    private final int maxVideosPorChamado;
    private final int maxDocumentosPorEquipamento;

    public AnexoService(AnexoRepository anexoRepository,
                        ChamadoRepository chamadoRepository,
                        EquipamentoUsuarioRepository equipamentoUsuarioRepository,
                        UsuarioRepository usuarioRepository,
                        ArmazenamentoService armazenamento,
                        @Value("${app.upload.max-bytes:10485760}") long tamanhoMaximo,
                        @Value("${app.upload.max-fotos-chamado:5}") int maxFotosPorChamado,
                        @Value("${app.upload.max-videos-chamado:1}") int maxVideosPorChamado,
                        @Value("${app.upload.max-documentos-equipamento:5}") int maxDocumentosPorEquipamento) {
        this.anexoRepository = anexoRepository;
        this.chamadoRepository = chamadoRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.armazenamento = armazenamento;
        this.tamanhoMaximo = tamanhoMaximo;
        this.maxFotosPorChamado = maxFotosPorChamado;
        this.maxVideosPorChamado = maxVideosPorChamado;
        this.maxDocumentosPorEquipamento = maxDocumentosPorEquipamento;
    }

    // ---------- chamado ----------

    @Transactional
    public List<AnexoDto> anexarAoChamado(Long chamadoId, List<MultipartFile> arquivos, AutorAutenticado autor) {
        Chamado chamado = chamadoDoCliente(chamadoId, autor);
        if (chamado.statusAtual() == StatusChamadoEnum.FINALIZADO) {
            throw new ConflitoException("Chamado finalizado é somente leitura.");
        }
        exigirArquivos(arquivos);
        long fotos = anexoRepository.countByChamadoIdAndCategoria(chamadoId, CategoriaAnexo.FOTO);
        long videos = anexoRepository.countByChamadoIdAndCategoria(chamadoId, CategoriaAnexo.VIDEO);
        Usuario autorEntidade = usuario(autor);
        List<AnexoDto> salvos = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            String tipo = validar(arquivo, TIPOS_CHAMADO, "Use JPG, PNG ou MP4.");
            boolean video = tipo.equals("video/mp4");
            if (video && ++videos > maxVideosPorChamado) {
                throw new RegraDeNegocioException("Limite de " + maxVideosPorChamado + " vídeo por chamado.");
            }
            if (!video && ++fotos > maxFotosPorChamado) {
                throw new RegraDeNegocioException("Limite de " + maxFotosPorChamado + " fotos por chamado.");
            }
            Anexo anexo = novo(arquivo, tipo, video ? CategoriaAnexo.VIDEO : CategoriaAnexo.FOTO, autorEntidade);
            anexo.setChamado(chamado);
            anexo.setCaminho(armazenamento.gravar("chamados/" + chamadoId, arquivo, tipo));
            salvos.add(paraDto(anexoRepository.save(anexo)));
        }
        return salvos;
    }

    @Transactional(readOnly = true)
    public List<AnexoDto> listarDoChamado(Long chamadoId, AutorAutenticado autor) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado."));
        if (autor.ehCliente() && !chamado.getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Chamado não encontrado.");
        }
        return anexoRepository.findByChamadoIdOrderByIdAsc(chamadoId).stream().map(this::paraDto).toList();
    }

    // ---------- equipamento do cliente ----------

    @Transactional
    public List<AnexoDto> anexarAoEquipamento(Long unidadeId, List<MultipartFile> arquivos, AutorAutenticado autor) {
        EquipamentoUsuario unidade = unidadeDoCliente(unidadeId, autor);
        if (!unidade.getEquipamento().getFabricante().isPropria()) {
            throw new RegraDeNegocioException("Nota fiscal e comprovantes só valem para equipamentos da marca própria (FrigoPro).");
        }
        exigirArquivos(arquivos);
        long existentes = anexoRepository.countByEquipamentoUsuarioId(unidadeId);
        if (existentes + arquivos.size() > maxDocumentosPorEquipamento) {
            throw new RegraDeNegocioException("Limite de " + maxDocumentosPorEquipamento + " documentos por equipamento.");
        }
        Usuario autorEntidade = usuario(autor);
        List<AnexoDto> salvos = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            String tipo = validar(arquivo, TIPOS_DOCUMENTO, "Use PDF, JPG ou PNG.");
            Anexo anexo = novo(arquivo, tipo, CategoriaAnexo.DOCUMENTO, autorEntidade);
            anexo.setEquipamentoUsuario(unidade);
            anexo.setCaminho(armazenamento.gravar("equipamentos/" + unidadeId, arquivo, tipo));
            salvos.add(paraDto(anexoRepository.save(anexo)));
        }
        return salvos;
    }

    @Transactional(readOnly = true)
    public List<AnexoDto> listarDoEquipamento(Long unidadeId, AutorAutenticado autor) {
        EquipamentoUsuario unidade = equipamentoUsuarioRepository.findById(unidadeId)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Equipamento não encontrado."));
        if (autor.ehCliente() && !unidade.getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Equipamento não encontrado.");
        }
        return anexoRepository.findByEquipamentoUsuarioIdOrderByIdAsc(unidadeId).stream().map(this::paraDto).toList();
    }

    /** Ao excluir a unidade, os documentos dela também saem (banco e disco). */
    @Transactional
    public void apagarDocumentosDoEquipamento(Long unidadeId) {
        List<Anexo> docs = anexoRepository.findByEquipamentoUsuarioIdOrderByIdAsc(unidadeId);
        docs.forEach((a) -> armazenamento.apagar(a.getCaminho()));
        anexoRepository.deleteAll(docs);
    }

    // ---------- download ----------

    @Transactional(readOnly = true)
    public Anexo buscarParaDownload(Long anexoId, AutorAutenticado autor) {
        Anexo anexo = anexoRepository.findById(anexoId).orElseThrow(() -> new BuscaNaoEncontradaException("Arquivo não encontrado."));
        if (autor.ehCliente()) {
            Usuario dono = anexo.getChamado() != null ? anexo.getChamado().getUsuario() : anexo.getEquipamentoUsuario().getUsuario();
            if (!dono.getId().equals(autor.id())) {
                throw new BuscaNaoEncontradaException("Arquivo não encontrado.");
            }
        }
        return anexo;
    }

    public Resource conteudo(Anexo anexo) {
        Resource r = armazenamento.ler(anexo.getCaminho());
        if (r == null) throw new BuscaNaoEncontradaException("Arquivo não encontrado.");
        return r;
    }

    // ---------- apoio ----------

    private Chamado chamadoDoCliente(Long id, AutorAutenticado autor) {
        Chamado chamado = chamadoRepository.findById(id).orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado."));
        if (!chamado.getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Chamado não encontrado.");
        }
        return chamado;
    }

    private EquipamentoUsuario unidadeDoCliente(Long id, AutorAutenticado autor) {
        EquipamentoUsuario unidade = equipamentoUsuarioRepository.findById(id)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Equipamento não encontrado."));
        if (!unidade.getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Equipamento não encontrado.");
        }
        return unidade;
    }

    private Usuario usuario(AutorAutenticado autor) {
        return usuarioRepository.findById(autor.id()).orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado."));
    }

    private void exigirArquivos(List<MultipartFile> arquivos) {
        if (arquivos == null || arquivos.isEmpty() || arquivos.stream().allMatch(MultipartFile::isEmpty)) {
            throw new RegraDeNegocioException("Envie ao menos um arquivo.");
        }
    }

    /** Tamanho, tipo permitido e assinatura do conteúdo precisam estar de acordo. Devolve o tipo verificado. */
    private String validar(MultipartFile arquivo, Set<String> permitidos, String dica) {
        if (arquivo.isEmpty()) {
            throw new RegraDeNegocioException("O arquivo " + nome(arquivo) + " está vazio.");
        }
        if (arquivo.getSize() > tamanhoMaximo) {
            throw new RegraDeNegocioException("O arquivo " + nome(arquivo) + " passa do limite de " + (tamanhoMaximo / 1048576) + " MB.");
        }
        String tipo = arquivo.getContentType() == null ? "" : arquivo.getContentType().toLowerCase();
        if (!permitidos.contains(tipo) || !ArmazenamentoService.assinaturaConfere(tipo, armazenamento.cabecalho(arquivo))) {
            throw new RegraDeNegocioException("Tipo de arquivo não aceito em " + nome(arquivo) + ". " + dica);
        }
        return tipo;
    }

    private Anexo novo(MultipartFile arquivo, String tipo, CategoriaAnexo categoria, Usuario autor) {
        Anexo anexo = new Anexo();
        anexo.setNomeOriginal(nome(arquivo));
        anexo.setTipoConteudo(tipo);
        anexo.setTamanho(arquivo.getSize());
        anexo.setCategoria(categoria);
        anexo.setCriadoEm(LocalDateTime.now());
        anexo.setEnviadoPor(autor);
        return anexo;
    }

    private String nome(MultipartFile arquivo) {
        String n = arquivo.getOriginalFilename();
        if (n == null || n.isBlank()) return "arquivo";
        n = n.replace("\\", "/");
        n = n.substring(n.lastIndexOf('/') + 1);
        return n.length() > 120 ? n.substring(n.length() - 120) : n;
    }

    private AnexoDto paraDto(Anexo a) {
        return new AnexoDto(a.getId(), a.getNomeOriginal(), a.getTipoConteudo(), a.getTamanho(), a.getCategoria(),
                a.getCriadoEm(), "/anexos/" + a.getId() + "/arquivo");
    }
}
