package com.frigo.frigotec.email;

import java.math.BigDecimal;

public record OrcamentoSalvoEvent(Long mensagemId, BigDecimal valor) {
}