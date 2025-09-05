package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.finance.calcs.core.percent.Percent;

import java.io.IOException;

public class PercentSerializer extends StdSerializer<Percent> {
    public PercentSerializer() {
        this(null);
    }

    public PercentSerializer(Class<Percent> t) {
        super(t);
    }

    @Override
    public void serialize(
            Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        simpleSerialize(percent, jsonGenerator, serializer);
    }

    public void expansiveSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("percent", percent.toString());
        jsonGenerator.writeNumberField("asDouble", percent.asDouble());
        jsonGenerator.writeEndObject();
    }

    public void simpleSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeString(percent.toString());
    }
}
