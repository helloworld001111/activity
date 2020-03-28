package com.example.dubei.activity.base.json;

import com.example.dubei.activity.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.Date;

public class DateTimeSerialize extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(DateUtils.formatDateTime(date));
    }

}
