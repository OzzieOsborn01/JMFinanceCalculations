package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.finance.calcs.core.percent.Percent;

import java.io.IOException;

/**
 * Serializer to convert {@link Percent} into a JSON object
 */
public class PercentSerializer extends StdSerializer<Percent> {
    public PercentSerializer() {
        this(null);
    }

    public PercentSerializer(Class<Percent> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(
            Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        simpleSerialize(percent, jsonGenerator, serializer);
    }

    /**
     * Expansive Serialization that will display both percent ("17.95%") and as double values (0.1795)
     */
    public void expansiveSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("percent", percent.toString());
        jsonGenerator.writeNumberField("asDouble", percent.asDouble());
        jsonGenerator.writeEndObject();
    }

    /**
     * Simple serialize function that outputs the percent as a percent string "19.75%"
     */
    public void simpleSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeString(percent.toString());
    }
}
