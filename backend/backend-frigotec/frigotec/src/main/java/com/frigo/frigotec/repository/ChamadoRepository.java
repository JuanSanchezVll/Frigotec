package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByUsuarioId(UUID idUsuario);

    List<Chamado> findByTecnicoId(UUID idTecnico);

    /** O equipamento com chamado não pode ser excluído: o histórico do atendimento é preservado. */
    boolean existsByEquipamentoUsuarioId(Long equipamentoUsuarioId);
}
