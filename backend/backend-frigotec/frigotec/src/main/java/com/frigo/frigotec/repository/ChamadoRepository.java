package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.util.StatusChamadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findbyUsuarioId(UUID idUsuario);

    List<Chamado> findByHistoricosStatus(StatusChamadoEnum status);}
