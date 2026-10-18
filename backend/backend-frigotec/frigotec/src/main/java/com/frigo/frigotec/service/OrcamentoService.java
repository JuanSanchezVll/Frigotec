package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.OrcamentoCadastroDto;
import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.Orcamento;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.OrcamentoMapper;
import com.frigo.frigotec.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public OrcamentoDto save(OrcamentoCadastroDto novoOrcamento) {

        Chamado chamado = novoOrcamento.getChamado();
        Orcamento orcamento = orcamentoRepository.save(OrcamentoMapper.toEntity(novoOrcamento, chamado));

        OrcamentoDto orcamentoSalvo = OrcamentoMapper.toResponse(orcamento, chamado);

        return orcamentoSalvo;
    }

    public OrcamentoDto searchOrcamentoForId(Long id) {
        Optional<Orcamento> orcamentoEncontrado = orcamentoRepository.findById(id);

        if (orcamentoEncontrado.isEmpty()) {
            throw new BuscaNaoEncontradaException("Orçamento não encontrado!");
        }

        Orcamento orcamento = orcamentoEncontrado.get();

        OrcamentoDto orcamentoDto = new OrcamentoDto();
        orcamentoDto.setValor(orcamento.getValor());

        ChamadoDto chamado = OrcamentoMapper.toResponse(orcamento, orcamento.getChamado());

        orcamentoDto.setChamado(chamado);
        orcamentoDto.setAceito(orcamento.getAceito());

        return orcamentoDto;
    }
}
