package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Perfil;

/** Resposta do login (SDD seção 8: "devolve token e perfil"). O usuário vem junto para a tela mostrar o nome. */
public record LoginRespostaDto(String token, String tipo, long expiraEm, Perfil perfil, UsuarioDto usuario) {}
