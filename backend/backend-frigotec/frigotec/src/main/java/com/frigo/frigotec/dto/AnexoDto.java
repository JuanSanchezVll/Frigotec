package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.CategoriaAnexo;

import java.time.LocalDateTime;

/** Metadados do arquivo; o conteúdo é baixado em GET {url} (com o token JWT). */
public record AnexoDto(Long id, String nome, String tipoConteudo, long tamanho, CategoriaAnexo categoria,
                       LocalDateTime criadoEm, String url) {
}
