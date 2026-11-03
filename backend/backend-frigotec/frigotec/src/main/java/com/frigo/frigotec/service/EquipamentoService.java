package com.frigo.frigotec.service;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Garantia;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.AcessoNegadoException;
import com.frigo.frigotec.exception.BuscaEncontradaException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.exception.RegraDeNegocioException;
import com.frigo.frigotec.mapper.EquipamentoMapper;
import com.frigo.frigotec.repository.EquipamentoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.FabricanteRepository;
import com.frigo.frigotec.repository.GarantiaRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final FabricanteRepository fabricanteRepository;
    private final GarantiaRepository garantiaRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository,
                              EquipamentoUsuarioRepository equipamentoUsuarioRepository,
                              UsuarioRepository usuarioRepository,
                              FabricanteRepository fabricanteRepository,
                              GarantiaRepository garantiaRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.fabricanteRepository = fabricanteRepository;
        this.garantiaRepository = garantiaRepository;
    }

    /**
     * Cadastro do equipamento do cliente (SDD RN-EUS-01 a 04). O dono é o usuário do token (RN-USR-06).
     * Marca própria (FrigoPro): tipo de garantia e data da compra são obrigatórios e a garantia nasce EM_ANALISE.
     * Outras marcas: esses campos não são aceitos (422).
     */
    @Transactional
    public EquipamentoUsuario salvarEquipamentoUsuario(EquipamentoUsuarioDto dto, UUID idUsuario, AutorAutenticado autor) {
        if (!autor.id().equals(idUsuario)) {
            throw new AcessoNegadoException("Você só pode cadastrar equipamentos para o seu próprio usuário.");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Usuário não encontrado."));
        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new BuscaNaoEncontradaException("Não trabalhamos com o equipamento cadastrado"));

        String serie = dto.getNumeroSerie() == null || dto.getNumeroSerie().isBlank() ? null : dto.getNumeroSerie().trim();
        if (serie != null && equipamentoUsuarioRepository.existsByEquipamentoIdAndNumeroSerieIgnoreCase(equipamento.getId(), serie)) {
            throw new BuscaEncontradaException("Esse número de série já está cadastrado para este modelo.");
        }

        boolean propria = equipamento.getFabricante() != null && equipamento.getFabricante().isPropria();
        boolean enviouGarantia = dto.getTipoGarantia() != null || dto.getDataCompra() != null || dto.getNotaFiscal() != null;
        if (!propria && enviouGarantia) {
            throw new RegraDeNegocioException("Garantia, data da compra e nota fiscal só valem para equipamentos da marca própria.");
        }
        if (propria && (dto.getTipoGarantia() == null || dto.getTipoGarantia().isBlank() || dto.getDataCompra() == null)) {
            throw new RegraDeNegocioException("Para equipamentos da marca própria informe o tipo de garantia e a data da compra.");
        }

        EquipamentoUsuario equipamentoUsuario = new EquipamentoUsuario();
        equipamentoUsuario.setEquipamento(equipamento);
        equipamentoUsuario.setUsuario(usuario);
        equipamentoUsuario.setNumeroSerie(serie);
        equipamentoUsuario = equipamentoUsuarioRepository.save(equipamentoUsuario);

        if (propria) {
            Garantia garantia = new Garantia();
            garantia.setEquipamentoUsuario(equipamentoUsuario);
            garantia.setTipo(dto.getTipoGarantia());
            garantia.setDataCompra(dto.getDataCompra());
            garantia.setNotaFiscal(dto.getNotaFiscal());
            equipamentoUsuario.setGarantia(garantiaRepository.save(garantia));
        }
        return equipamentoUsuario;
    }

    /** SDD RN-EUS-06: o cliente vê só os próprios equipamentos; técnico e admin veem os de qualquer cliente. */
    public List<EquipamentoUsuario> listarPorUsuario(UUID idUsuario, AutorAutenticado autor) {
        if (autor.ehCliente() && !autor.id().equals(idUsuario)) {
            throw new AcessoNegadoException("Você só pode ver os seus próprios equipamentos.");
        }
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new BuscaNaoEncontradaException("Usuário não encontrado.");
        }
        return equipamentoUsuarioRepository.findByUsuarioId(idUsuario);
    }

    /** SDD RN-EQP-04: modelos da marca escolhida. */
    public List<Equipamento> listarPorFabricante(Long idFabricante) {
        if (!fabricanteRepository.existsById(idFabricante)) {
            throw new BuscaNaoEncontradaException("Fabricante não encontrado.");
        }
        return equipamentoRepository.findByFabricanteId(idFabricante);
    }

    /** SDD RN-EQP-02: cadastro de modelo do catálogo (equipe Frigotec). */
    @Transactional
    public Equipamento salvarEquipamento(EquipamentoCadastroDto equipamentoDto) {
        if (!fabricanteRepository.existsById(equipamentoDto.getFabricanteId())) {
            throw new BuscaNaoEncontradaException("Fabricante não encontrado.");
        }
        if (equipamentoRepository.existsByModeloIgnoreCaseAndFabricanteId(equipamentoDto.getModelo(), equipamentoDto.getFabricanteId())) {
            throw new BuscaEncontradaException("Esse modelo de equipamento já existe");
        }
        return equipamentoRepository.save(EquipamentoMapper.toEntityCadastro(equipamentoDto));
    }
}
