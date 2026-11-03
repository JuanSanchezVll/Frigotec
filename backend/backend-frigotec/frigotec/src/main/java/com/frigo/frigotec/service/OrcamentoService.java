package com.frigo.frigotec.service;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.OrcamentoCadastroDto;
import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.dto.RespostaOrcamentoDto;
import com.frigo.frigotec.email.OrcamentoSalvoEvent;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.MensagemEmail;
import com.frigo.frigotec.entity.Orcamento;
import com.frigo.frigotec.exception.AcessoNegadoException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.exception.ConflitoException;
import com.frigo.frigotec.mapper.OrcamentoMapper;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.MensagemEmailRepository;
import com.frigo.frigotec.repository.OrcamentoRepository;
import com.frigo.frigotec.util.StatusChamadoEnum;
import com.frigo.frigotec.util.StatusEnvio;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.LocalDateTime;

/** Regras do orçamento (SDD seção 6). */
@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ChamadoRepository chamadoRepository;
    private final MensagemEmailRepository mensagemEmailRepository;
    private final ApplicationEventPublisher publisher;
    private final OrcamentoMapper mapper;

    public OrcamentoService(OrcamentoRepository orcamentoRepository,
                            ChamadoRepository chamadoRepository,
                            MensagemEmailRepository mensagemEmailRepository,
                            ApplicationEventPublisher publisher,
                            OrcamentoMapper mapper) {
        this.orcamentoRepository = orcamentoRepository;
        this.chamadoRepository = chamadoRepository;
        this.mensagemEmailRepository = mensagemEmailRepository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    /** RN-ORC-01 a 05: técnico responsável (ou admin), chamado em VISITA_AGENDADA com a visita já passada, um pendente por vez. */
    @Transactional
    public OrcamentoDto salvar(OrcamentoCadastroDto dto, AutorAutenticado autor) {
        Chamado chamado = chamadoRepository.findById(dto.getChamadoId())
                .orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado."));
        boolean responsavel = chamado.getTecnico() != null && chamado.getTecnico().getId().equals(autor.id());
        if (!autor.ehAdmin() && !responsavel) {
            throw new AcessoNegadoException("Somente o técnico responsável pelo chamado (ou um administrador) cria orçamento.");
        }
        if (chamado.statusAtual() != StatusChamadoEnum.VISITA_AGENDADA) {
            throw new ConflitoException("O orçamento só pode ser criado com o chamado em VISITA_AGENDADA. Status atual: " + chamado.statusAtual() + ".");
        }
        if (chamado.getVisitaAgendadaEm() == null || chamado.getVisitaAgendadaEm().isAfter(LocalDateTime.now())) {
            throw new ConflitoException("O orçamento só pode ser criado depois que a visita acontecer.");
        }
        if (orcamentoRepository.existePendenteNoChamado(chamado.getId())) {
            throw new ConflitoException("Já existe um orçamento pendente de resposta neste chamado.");
        }

        Orcamento orcamento = new Orcamento();
        orcamento.setChamado(chamado);
        orcamento.setValor(dto.getValor().setScale(2, RoundingMode.HALF_UP));
        orcamento.setDescricao(dto.getDescricao().trim());
        orcamento.setCriadoEm(LocalDateTime.now());
        orcamento.setAceito(null);
        orcamento = orcamentoRepository.save(orcamento);

        if (chamado.getUsuario() != null && chamado.getUsuario().getEmail() != null) {
            MensagemEmail mensagem = new MensagemEmail();
            mensagem.setChamado(chamado);
            mensagem.setUsuario(chamado.getUsuario());
            mensagem.setAssunto("Orçamento do chamado " + chamado.getProtocolo());
            mensagem.setDescricao("Orçamento enviado ao cliente");
            mensagem.setStatus(StatusEnvio.PENDENTE);
            mensagem = mensagemEmailRepository.save(mensagem);
            publisher.publishEvent(new OrcamentoSalvoEvent(mensagem.getId(), orcamento.getValor()));
        }
        return mapper.toResponse(orcamento);
    }

    /** RN-ORC-10: o cliente só vê orçamento do próprio chamado; de outro cliente retorna 404. */
    @Transactional(readOnly = true)
    public OrcamentoDto buscarOrcamentoPorId(Long id, AutorAutenticado autor) {
        return mapper.toResponse(buscarVisivel(id, autor));
    }

    /** RN-ORC-06: só o cliente dono responde, só se estiver PENDENTE, e a resposta é definitiva. */
    @Transactional
    public OrcamentoDto responder(Long id, RespostaOrcamentoDto dto, AutorAutenticado autor) {
        Orcamento orcamento = buscarVisivel(id, autor);
        if (orcamento.getAceito() != null) {
            throw new ConflitoException("Este orçamento já foi respondido.");
        }
        orcamento.setAceito(dto.getAceito());
        orcamento.setRespondidoEm(LocalDateTime.now());
        return mapper.toResponse(orcamentoRepository.save(orcamento));
    }

    private Orcamento buscarVisivel(Long id, AutorAutenticado autor) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Orçamento não encontrado."));
        if (autor.ehCliente() && !orcamento.getChamado().getUsuario().getId().equals(autor.id())) {
            throw new BuscaNaoEncontradaException("Orçamento não encontrado.");
        }
        return orcamento;
    }
}
