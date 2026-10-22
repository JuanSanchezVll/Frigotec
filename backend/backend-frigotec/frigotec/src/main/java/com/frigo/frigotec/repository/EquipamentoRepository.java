package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Boolean existsByModeloIgnoreCaseAndFabricanteId(String nome, Long fabricanteId);
}
