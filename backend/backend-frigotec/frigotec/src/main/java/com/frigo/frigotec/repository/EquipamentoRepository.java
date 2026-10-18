package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.Fabricante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Equipamento findByModeloAndFabricante(String modelo , String fabricante);
}
