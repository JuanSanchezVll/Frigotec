package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    /** Orçamentos ainda sem resposta do cliente (isAceito nulo). */
    @Query("select o from Orcamento o where o.isAceito is null")
    List<Orcamento> pendentes();

    /** Existe orçamento sem resposta (PENDENTE) neste chamado? (SDD RN-ORC-05 e RN-CHM-15) */
    @Query("select count(o) > 0 from Orcamento o where o.chamado.id = :chamadoId and o.isAceito is null")
    boolean existePendenteNoChamado(@Param("chamadoId") Long chamadoId);
}
