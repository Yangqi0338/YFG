package com.base.sbc.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalJsonSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            double doubleValue = value.doubleValue();
            int intValue = value.intValue();
            if (doubleValue == (double) intValue) {
                gen.writeString(value.setScale(1, RoundingMode.HALF_UP).toString());
            }else {
                gen.writeString(value.setScale(BigDecimal.valueOf(doubleValue).scale(), RoundingMode.HALF_UP).toString());
            }
        }
    }
}