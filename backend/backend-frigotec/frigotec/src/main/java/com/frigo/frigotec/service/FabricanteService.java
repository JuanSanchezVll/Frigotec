package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.FabricanteCadastroDto;
import com.frigo.frigotec.dto.FabricanteDto;
import com.frigo.frigotec.entity.Fabricante;
import com.frigo.frigotec.exception.BuscaEncontradaException;
import com.frigo.frigotec.mapper.FabricanteMapper;
import com.frigo.frigotec.repository.FabricanteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class FabricanteService {
    @Autowired
    private final FabricanteRepository repository;


    public FabricanteService(FabricanteRepository repository) {
        this.repository = repository;
    }


@Transactional
    public FabricanteDto salvar (FabricanteCadastroDto fabricanteCadastroDto) {

        if(repository.existsByNomeIgnoreCase(fabricanteCadastroDto.getNome())) {
            throw new BuscaEncontradaException("Já existe uma empresa com esse nome");
        }

        Fabricante fabricante = FabricanteMapper.toEntityCadastro(fabricanteCadastroDto);
      FabricanteDto dto = FabricanteMapper.toDto( repository.save(fabricante));



        return dto;
    }


}
