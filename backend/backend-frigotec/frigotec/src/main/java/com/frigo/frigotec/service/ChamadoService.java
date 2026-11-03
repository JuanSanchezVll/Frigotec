package com.frigo.frigotec.service;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.AtribuicaoDto;
import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.EncerramentoDto;
import com.frigo.frigotec.dto.StatusChamadoAlteradoEvento;
import com.frigo.frigotec.dto.VisitaDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.Encerramento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Historico;
import com.frigo.frigotec.entity.Perfil;
import com.frigo.frigotec.entity.Redesignacao;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.AcessoNegadoException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.exception.ConflitoException;
import com.frigo.frigotec.exception.RegraDeNegocioException;
import com.frigo.frigotec.mapper.ChamadoMapper;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.EncerramentoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.OrcamentoRepository;
import com.frigo.frigotec.repository.RedesignacaoRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import com.frigo.frigotec.util.StatusChamadoEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Regras do chamado (SDD seção 5). Quem pode o quê é decidido aqui, com o usuário que vem do token. */
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;
    private final EncerramentoRepository encerramentoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final RedesignacaoRepository redesignacaoRepository;
    private final ChamadoMapper mapper;
    private final ApplicationEventPublisher publisher;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          UsuarioRepository usuarioRepository,
                          EquipamentoUsuarioRepository equipamentoUsuarioRepository,
                          EncerramentoRepository encerramentoRepository,
                          OrcamentoRepository orcamentoRepository,
                          RedesignacaoRepository redesignacaoRepository,
                          ChamadoMapper mapper,
                          ApplicationEventPublisher publisher) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.encerramentoRepository = encerramentoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.redesignacaoRepository = redesignacaoRepository;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    // ---------- abertura ----------

    /** RN-CHM-01 a 05: o equipamento precisa ser do cliente logado; nasce ENVIADO, sem técnico, com protocolo PR+8 dígitos. */
    @Transactional
    public ChamadoDto salvar(ChamadoCadastroDto dto, AutorAutenticado autor) {
        Usuario usuario = usuario(autor.id());
        EquipamentoUsuario equipamentoUsuario = equipamentoUsuarioRepository.findById(dto.getEquipamentoUsuarioId())
                .filter(eu -> eu.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new BuscaNaoEncontradaException("Equipamento não encontrado."));

        Chamado chamado = mapper.toEntityCadastro(dto);
        chamado.setDataPedido(LocalDate.now());
        chamado.setUsuario(usuario);
        chamado.setEquipamentoUsuario(equipamentoUsuario);
        chamado.adicionarHistorico(novoHistorico(StatusChamadoEnum.ENVIADO, usuario));
        chamado = chamadoRepository.save(chamado);
        chamado.setProtocolo(String.format("PR%08d", chamado.getId()));
        return mapper.toDto(chamadoRepository.save(chamado));
    }

    // ---------- consultas ----------

    /** RN-CHM-17: sempre pelo status atual (último histórico). Só a equipe lista todos. */
    @Transactional(readOnly = true)
    public List<ChamadoDto> listar(StatusChamadoEnum status, boolean semTecnico) {
        return chamadoRepository.findAll().stream()
                .filter(c -> status == null || c.statusAtual() == status)
                .filter(c -> !semTecnico || c.getTecnico() == null)
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChamadoDto buscarPorId(Long id, AutorAutenticado autor) {
        return mapper.toDto(buscarEntidade(id, autor));
    }

    @Transactional(readOnly = true)
    public List<ChamadoDto> buscarPorUsuario(UUID idUsuario, String situacao, AutorAutenticado autor) {
        if (autor.ehCliente() && !autor.id().equals(idUsuario)) {
            throw new AcessoNegadoException("Você só pode ver os seus próprios chamados.");
        }
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new BuscaNaoEncontradaException("Usuário não encontrado.");
        }
        return chamadoRepository.findByUsuarioId(idUsuario).stream()
                .filter(c -> combinaComSituacao(c, situacao))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChamadoDto> buscarPorTecnico(UUID idTecnico, AutorAutenticado autor) {
        if (!autor.ehAdmin() && !autor.id().equals(idTecnico)) {
            throw new AcessoNegadoException("Você só pode ver os chamados atribuídos a você.");
        }
        return chamadoRepository.findByTecnicoId(idTecnico).stream().map(mapper::toDto).toList();
    }

    // ---------- transições ----------

    /** RN-CHM-10/11: só o próximo status da sequência, e só por quem a regra permite. */
    @Transactional
    public ChamadoDto atualizarStatus(Long id, StatusChamadoEnum novoStatus, AutorAutenticado autor) {
        Chamado chamado = buscarEntidade(id, autor);
        exigirAberto(chamado);
        StatusChamadoEnum atual = chamado.statusAtual();
        StatusChamadoEnum proximo = StatusChamadoEnum.values()[atual.ordinal() + 1];
        if (novoStatus != proximo) {
            throw new ConflitoException("Transição inválida. Status atual: " + atual + ". Próximo permitido: " + proximo + ".");
        }
        Usuario responsavel = usuario(autor.id());
        switch (novoStatus) {
            case APROVADO_PELO_TECNICO -> {
                if (!autor.daEquipe()) {
                    throw new AcessoNegadoException("Somente a equipe Frigotec aprova o chamado.");
                }
                if (autor.ehAdmin()) {
                    throw new RegraDeNegocioException("Para aprovar como administrador use POST /chamados/{id}/atribuicao informando o técnico.");
                }
                chamado.setTecnico(responsavel);
            }
            case CONTATO_DO_TECNICO -> exigirResponsavel(chamado, autor);
            case APROVACAO_DO_SERVICO -> {
                if (!autor.ehCliente()) {
                    throw new AcessoNegadoException("Somente o cliente dono do chamado aprova o serviço.");
                }
            }
            case VISITA_AGENDADA -> throw new RegraDeNegocioException("Agende a visita em PATCH /chamados/{id}/visita (data e hora são obrigatórias).");
            default -> throw new RegraDeNegocioException("Finalize o chamado em POST /chamados/{id}/encerramento.");
        }
        return registrar(chamado, novoStatus, responsavel);
    }

    /** RN-CHM-12: "atribuir a mim" (técnico), atribuir a qualquer colaborador (admin) ou redesignar com motivo. */
    @Transactional
    public ChamadoDto atribuir(Long id, AtribuicaoDto dto, AutorAutenticado autor) {
        Chamado chamado = buscarEntidade(id, autor);
        exigirAberto(chamado);
        Usuario responsavel = usuario(autor.id());

        if (chamado.getTecnico() == null) {
            if (chamado.statusAtual() != StatusChamadoEnum.ENVIADO) {
                throw new ConflitoException("Só chamados em ENVIADO e sem técnico podem ser atribuídos. Status atual: " + chamado.statusAtual() + ".");
            }
            chamado.setTecnico(alvoDaAtribuicao(dto, autor, responsavel));
            return registrar(chamado, StatusChamadoEnum.APROVADO_PELO_TECNICO, responsavel);
        }

        if (!autor.ehAdmin()) {
            exigirResponsavel(chamado, autor);
        }
        if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
            throw new RegraDeNegocioException("Informe o motivo da redesignação.");
        }
        if (dto.getTecnicoId() == null) {
            throw new RegraDeNegocioException("Informe o novo técnico.");
        }
        Usuario novo = colaborador(dto.getTecnicoId());
        if (novo.getId().equals(chamado.getTecnico().getId())) {
            throw new ConflitoException("O chamado já está com esse técnico.");
        }
        Redesignacao redesignacao = new Redesignacao();
        redesignacao.setChamado(chamado);
        redesignacao.setTecnicoAnterior(chamado.getTecnico());
        redesignacao.setTecnicoNovo(novo);
        redesignacao.setMotivo(dto.getMotivo().trim());
        redesignacao.setFeitoPor(responsavel);
        redesignacao.setFeitoEm(LocalDateTime.now());
        redesignacaoRepository.save(redesignacao);
        chamado.setTecnico(novo);
        return mapper.toDto(chamadoRepository.save(chamado));
    }

    /** RN-CHM-14: data e hora futuras; a partir de APROVACAO_DO_SERVICO; reagendar não cria novo status. */
    @Transactional
    public ChamadoDto agendarVisita(Long id, VisitaDto dto, AutorAutenticado autor) {
        Chamado chamado = buscarEntidade(id, autor);
        exigirAberto(chamado);
        exigirResponsavel(chamado, autor);
        if (!dto.getVisitaAgendadaEm().isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException("A visita precisa ter data e hora futuras.");
        }
        StatusChamadoEnum atual = chamado.statusAtual();
        if (atual.ordinal() < StatusChamadoEnum.APROVACAO_DO_SERVICO.ordinal()) {
            throw new ConflitoException("A visita só pode ser agendada a partir de APROVACAO_DO_SERVICO. Status atual: " + atual + ".");
        }
        chamado.setVisitaAgendadaEm(dto.getVisitaAgendadaEm());
        if (atual == StatusChamadoEnum.APROVACAO_DO_SERVICO) {
            return registrar(chamado, StatusChamadoEnum.VISITA_AGENDADA, usuario(autor.id()));
        }
        chamadoRepository.save(chamado);
        publisher.publishEvent(new StatusChamadoAlteradoEvento(chamado, StatusChamadoEnum.VISITA_AGENDADA));
        return mapper.toDto(chamado);
    }

    /** RN-CHM-15: só em VISITA_AGENDADA, sem orçamento pendente, com motivo e descrição da solução. */
    @Transactional
    public ChamadoDto encerrar(Long id, EncerramentoDto dto, AutorAutenticado autor) {
        Chamado chamado = buscarEntidade(id, autor);
        exigirAberto(chamado);
        if (!autor.ehAdmin()) {
            exigirResponsavel(chamado, autor);
        }
        if (chamado.statusAtual() != StatusChamadoEnum.VISITA_AGENDADA) {
            throw new ConflitoException("O chamado só pode ser encerrado em VISITA_AGENDADA. Status atual: " + chamado.statusAtual() + ".");
        }
        if (orcamentoRepository.existePendenteNoChamado(chamado.getId())) {
            throw new ConflitoException("Existe orçamento pendente de resposta do cliente.");
        }
        Encerramento encerramento = new Encerramento();
        encerramento.setChamado(chamado);
        encerramento.setMotivo(dto.getMotivo().trim());
        encerramento.setDescricao(dto.getDescricao().trim());
        encerramento.setDataEncerramento(LocalDate.now());
        chamado.getEncerramentos().add(encerramentoRepository.save(encerramento));
        return registrar(chamado, StatusChamadoEnum.FINALIZADO, usuario(autor.id()));
    }

    // ---------- apoio ----------

    private Chamado buscarEntidade(Long id, AutorAutenticado autor) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado."));
        // 404 e não 403, para não revelar que o chamado de outro cliente existe.
        if (autor.ehCliente() && !chamado.getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Chamado não encontrado.");
        }
        return chamado;
    }

    private void exigirAberto(Chamado chamado) {
        if (chamado.statusAtual() == StatusChamadoEnum.FINALIZADO) {
            throw new ConflitoException("Chamado finalizado é somente leitura.");
        }
    }

    private void exigirResponsavel(Chamado chamado, AutorAutenticado autor) {
        if (chamado.getTecnico() == null || !chamado.getTecnico().getId().equals(autor.id())) {
            throw new AcessoNegadoException("Somente o técnico responsável pelo chamado pode executar esta ação.");
        }
    }

    private Usuario alvoDaAtribuicao(AtribuicaoDto dto, AutorAutenticado autor, Usuario responsavel) {
        if (dto.getTecnicoId() == null || dto.getTecnicoId().equals(autor.id())) {
            return responsavel;
        }
        if (!autor.ehAdmin()) {
            throw new AcessoNegadoException("O técnico só pode atribuir o chamado a si mesmo.");
        }
        return colaborador(dto.getTecnicoId());
    }

    private Usuario colaborador(UUID id) {
        Usuario usuario = usuario(id);
        if (usuario.getPerfil() == Perfil.CLIENTE) {
            throw new RegraDeNegocioException("O usuário informado não é um colaborador da Frigotec.");
        }
        return usuario;
    }

    private Usuario usuario(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new BuscaNaoEncontradaException("Usuário não encontrado."));
    }

    private Historico novoHistorico(StatusChamadoEnum status, Usuario responsavel) {
        Historico historico = new Historico();
        historico.setStatus(status);
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setResponsavel(responsavel);
        return historico;
    }

    /** RN-CHM-16/19: grava o histórico imutável e avisa o observador de e-mail depois da gravação. */
    private ChamadoDto registrar(Chamado chamado, StatusChamadoEnum novoStatus, Usuario responsavel) {
        chamado.adicionarHistorico(novoHistorico(novoStatus, responsavel));
        chamadoRepository.save(chamado);
        publisher.publishEvent(new StatusChamadoAlteradoEvento(chamado, novoStatus));
        return mapper.toDto(chamado);
    }

    private boolean combinaComSituacao(Chamado chamado, String situacao) {
        if (situacao == null || situacao.isBlank()) {
            return true;
        }
        boolean finalizado = chamado.statusAtual() == StatusChamadoEnum.FINALIZADO;
        return switch (situacao.toUpperCase()) {
            case "ANDAMENTO" -> !finalizado;
            case "FINALIZADO" -> finalizado;
            default -> throw new RegraDeNegocioException("Situação inválida. Use ANDAMENTO ou FINALIZADO.");
        };
    }
}
