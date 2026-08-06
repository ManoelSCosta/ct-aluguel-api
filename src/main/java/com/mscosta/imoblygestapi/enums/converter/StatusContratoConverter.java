package com.mscosta.imoblygestapi.enums.converter;

import com.mscosta.imoblygestapi.enums.StatusContrato;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusContratoConverter implements AttributeConverter<StatusContrato, String> {

    @Override
    public String convertToDatabaseColumn(StatusContrato status) {
        return status == null ? null : status.getCodigo();
    }

    @Override
    public StatusContrato convertToEntityAttribute(String codigo) {
        return codigo == null ? null : StatusContrato.fromCodigo(codigo.trim());
    }
}
