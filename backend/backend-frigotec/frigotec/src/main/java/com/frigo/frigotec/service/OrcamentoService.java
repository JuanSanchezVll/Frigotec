package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.email.OrcamentoSalvoEvent;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.MensagemEmail;
import com.frigo.frigotec.entity.Orcamento;
import com.frigo.frigotec.entity.StatusEnvio;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.OrcamentoMapper;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.MensagemEmailRepository;
import com.frigo.frigotec.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;
    private final ChamadoRepository chamadoRepository;
    private final MensagemEmailRepository mensagemEmailRepository;
    private final ApplicationEventPublisher publisher;

    private final OrcamentoMapper mapper;

    @Autowired
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

    @Transactional
    public OrcamentoDto salvar(OrcamentoDto novoOrcamento) {
        Chamado chamado = chamadoRepository.findById(novoOrcamento.getChamadoId())
                .orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado!"));

        Orcamento orcamento = new Orcamento();
        orcamento.setValor(novoOrcamento.getValor());
        orcamento.setChamado(chamado);
        orcamento.setAceito(null);
        orcamento = orcamentoRepository.save(orcamento);

        if (chamado.getUsuario() != null && chamado.getUsuario().getEmail() != null) {
            MensagemEmail mensagem = new MensagemEmail();
            mensagem.setChamado(chamado);
            mensagem.setUsuario(chamado.getUsuario());
            mensagem.setAssunto("Orçamento do chamado #" + chamado.getId());
            mensagem.setDescricao("Orçamento enviado ao cliente");
            mensagem.setStatus(StatusEnvio.PENDENTE);
            mensagem = mensagemEmailRepository.save(mensagem);

            publisher.publishEvent(new OrcamentoSalvoEvent(mensagem.getId(), orcamento.getValor()));
        }

        return mapper.toResponse(orcamento);
    }

    public OrcamentoDto buscarOrcamentoPorId(Long id) {
        Optional<Orcamento> orcamentoEncontrado = orcamentoRepository.findById(id);

        if (orcamentoEncontrado.isEmpty()) {
            throw new BuscaNaoEncontradaException("Orçamento não encontrado!");
        }

        Orcamento orcamento = orcamentoEncontrado.get();

        return mapper.toResponse(orcamento);
    }
}
