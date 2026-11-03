package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.GarantiaDto;
import com.frigo.frigotec.entity.Garantia;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class GarantiaMapper {

    /** RN-GAR-02: a situação é calculada. ATIVA quando validada e dataFimGarantia >= hoje; sem garantia = NAO_SE_APLICA. */
    public String situacao(Garantia garantia) {
        if (garantia == null) {
            return "NAO_SE_APLICA";
        }
        return switch (garantia.getValidacao()) {
            case RECUSADA -> "RECUSADA";
            case EM_ANALISE -> "EM_ANALISE";
            case VALIDADA -> (garantia.getDataFimGarantia() != null && !garantia.getDataFimGarantia().isBefore(LocalDate.now()))
                    ? "ATIVA" : "VENCIDA";
        };
    }

    public GarantiaDto toDto(Garantia garantia) {
        GarantiaDto dto = new GarantiaDto();
        dto.setSituacao(situacao(garantia));
        if (garantia != null) {
            dto.setId(garantia.getId());
            dto.setTipo(garantia.getTipo());
            dto.setDataCompra(garantia.getDataCompra());
            dto.setDataFimGarantia(garantia.getDataFimGarantia());
            dto.setNotaFiscal(garantia.getNotaFiscal());
        }
        return dto;
    }
}
