package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.util.StatusChamadoEnum;

public record StatusChamadoAlteradoEvento(Chamado chamado, StatusChamadoEnum novoStatus) {
}
