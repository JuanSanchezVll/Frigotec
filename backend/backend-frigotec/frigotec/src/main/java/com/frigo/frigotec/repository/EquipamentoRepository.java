package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Boolean existsByModeloIgnoreCaseAndFabricanteId(String nome, Long fabricanteId);

    List<Equipamento> findByFabricanteId(Long fabricanteId);
}
