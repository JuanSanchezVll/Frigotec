package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.EquipamentoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EquipamentoUsuarioRepository extends JpaRepository<EquipamentoUsuario, Long> {

    List<EquipamentoUsuario> findByUsuarioId(UUID usuarioId);

    boolean existsByEquipamentoIdAndNumeroSerieIgnoreCase(Long equipamentoId, String numeroSerie);
}
