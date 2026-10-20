package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.entity.Orcamento;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.OrcamentoMapper;
import com.frigo.frigotec.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;

    private final OrcamentoMapper mapper;

    @Autowired
    public OrcamentoService(OrcamentoRepository orcamentoRepository, OrcamentoMapper mapper) {
        this.orcamentoRepository = orcamentoRepository;
        this.mapper = mapper;
    }

    public OrcamentoDto save(OrcamentoDto novoOrcamento) {


        Orcamento orcamento = orcamentoRepository.save(mapper.toEntity(novoOrcamento));

        OrcamentoDto orcamentoSalvo = mapper.toResponse(orcamento);

        return orcamentoSalvo;
    }

    public OrcamentoDto searchOrcamentoForId(Long id) {
        Optional<Orcamento> orcamentoEncontrado = orcamentoRepository.findById(id);

        if (orcamentoEncontrado.isEmpty()) {
            throw new BuscaNaoEncontradaException("Orçamento não encontrado!");
        }

        Orcamento orcamento = orcamentoEncontrado.get();

        return mapper.toResponse(orcamento);
    }
}
