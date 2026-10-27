package com.frigo.frigotec.repository;

import com.frigo.frigotec.entity.MensagemEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MensagemEmailRepository extends JpaRepository<MensagemEmail, Long> {

    @Query("select MensagemDaEx from MensagemEmail MensagemDaEx join fetch" +
            " MensagemDaEx.usuario join fetch MensagemDaEx.chamado " +
            "where MensagemDaEx.id = :id")

    Optional<MensagemEmail> findComDetalhesById(@Param("id") Long id);

    List<MensagemEmail> findByChamadoId(Long chamadoId);
}