package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class PercentSerializerTest {
    private JsonGenerator jsonGenerator;
    private Writer jsonWriter;
    private SerializerProvider serializerProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup_SingleTest() throws IOException {
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void flush() throws IOException {
        jsonWriter.flush();
    }

    @Test
    public void expansiveSerialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        new PercentSerializer().expansiveSerialize(percent, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        Assertions.assertEquals("{\"percent\":\"25.0%\",\"asDouble\":0.25}", jsonWriter.toString());
    }

    @Test
    public void simpleSerialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        new PercentSerializer().simpleSerialize(percent, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        Assertions.assertEquals("\"25.0%\"", jsonWriter.toString());
    }

    @Test
    public void serialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        final String actual = objectMapper.writeValueAsString(percent);
        Assertions.assertEquals("\"25.0%\"", actual);
    }
}
