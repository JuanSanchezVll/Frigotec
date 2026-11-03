package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Anexo;
import com.frigo.frigotec.entity.CategoriaAnexo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {

    List<Anexo> findByChamadoIdOrderByIdAsc(Long chamadoId);

    List<Anexo> findByEquipamentoUsuarioIdOrderByIdAsc(Long equipamentoUsuarioId);

    long countByChamadoIdAndCategoria(Long chamadoId, CategoriaAnexo categoria);

    long countByEquipamentoUsuarioId(Long equipamentoUsuarioId);
}
