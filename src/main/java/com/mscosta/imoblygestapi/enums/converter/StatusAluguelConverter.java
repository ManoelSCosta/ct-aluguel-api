package com.mscosta.imoblygestapi.enums.converter;

import com.mscosta.imoblygestapi.enums.StatusAluguel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusAluguelConverter implements AttributeConverter<StatusAluguel, String> {

    @Override
    public String convertToDatabaseColumn(StatusAluguel status) {
        return status == null ? null : status.getCodigo();
    }

    @Override
    public StatusAluguel convertToEntityAttribute(String codigo) {
        return codigo == null ? null : StatusAluguel.fromCodigo(codigo.trim());
    }
}
