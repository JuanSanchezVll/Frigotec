package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.GarantiaDto;
import com.frigo.frigotec.dto.ValidacaoGarantiaDto;
import com.frigo.frigotec.entity.Garantia;
import com.frigo.frigotec.entity.ValidacaoGarantia;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.exception.RegraDeNegocioException;
import com.frigo.frigotec.mapper.GarantiaMapper;
import com.frigo.frigotec.repository.GarantiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GarantiaService {

    private final GarantiaRepository repository;
    private final GarantiaMapper mapper;

    public GarantiaService(GarantiaRepository repository, GarantiaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /** SDD RN-GAR-03: a equipe analisa os documentos e define VALIDADA (com a data de fim) ou RECUSADA. */
    @Transactional
    public GarantiaDto validar(Long id, ValidacaoGarantiaDto dto) {
        Garantia garantia = repository.findById(id)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Garantia não encontrada."));
        if (dto.getValidacao() == ValidacaoGarantia.EM_ANALISE) {
            throw new RegraDeNegocioException("Informe VALIDADA ou RECUSADA.");
        }
        if (dto.getValidacao() == ValidacaoGarantia.VALIDADA) {
            if (dto.getDataFimGarantia() == null || dto.getDataFimGarantia().isBefore(LocalDate.now())) {
                throw new RegraDeNegocioException("Para validar a garantia informe a data de fim (hoje ou futura).");
            }
            garantia.setDataFimGarantia(dto.getDataFimGarantia());
        } else {
            garantia.setDataFimGarantia(null);
        }
        garantia.setValidacao(dto.getValidacao());
        return mapper.toDto(repository.save(garantia));
    }
}
