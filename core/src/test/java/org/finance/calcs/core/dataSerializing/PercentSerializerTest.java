package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

public class PercentSerializerTest {
    private JsonGenerator jsonGenerator;
    private Writer jsonWriter;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestObject {
        @JsonProperty("percent")
        Percent percent;
    }

    @BeforeEach
    public void setup_SingleTest() throws IOException {
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        objectMapper = new ObjectMapper();
        serializerProvider = objectMapper.getSerializerProvider();
    }

    @AfterEach
    public void flush() throws IOException {
        jsonWriter.flush();
    }

    @Test
    public void expansiveSerialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        PercentSerializer.expansiveSerialize(percent, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        Assertions.assertEquals("{\"percent\":\"25.0%\",\"asDouble\":0.25}", jsonWriter.toString());
    }

    @Test
    public void simpleSerialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        PercentSerializer.simpleSerialize(percent, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        Assertions.assertEquals("\"25.0%\"", jsonWriter.toString());
    }

    @Test
    public void serialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        final String actual = objectMapper.writeValueAsString(percent);
        Assertions.assertEquals("\"25.0%\"", actual);
    }

    @Test
    public void nestedSerialization() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        final TestObject obj = new TestObject(percent);
        final String actual = objectMapper.writeValueAsString(obj);
        Assertions.assertEquals("{\"percent\":\"25.0%\"}", actual);
    }

    @Test
    public void expansiveDeserialize() throws IOException {
        final String inputString = "{\"percent\":\"25.0%\",\"asDouble\":0.25}";
        final InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        final JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        final Percent percent = PercentSerializer.expansiveDeserialize(jsonParser, null);
        Assertions.assertEquals(Percent.fromPercent(25.0), percent);
    }

    @Test
    public void expansiveDeserialize_decimalNull_percentNotNull() throws IOException {
        final String inputString = "{\"percent\":\"25.0%\",\"asDouble\":null}";
        final InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        final JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        final Percent percent = PercentSerializer.expansiveDeserialize(jsonParser, null);
        Assertions.assertEquals(Percent.fromPercent(25.0), percent);
    }

    @Test
    public void expansiveDeserialize_DecimalAndPercentNull() throws IOException {
        final String inputString = "{\"percent\":null,\"asDouble\":null}";
        final InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        final JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        Assertions.assertThrows(IOException.class, () -> PercentSerializer.expansiveDeserialize(jsonParser, null));
    }

    @Test
    public void expansiveDeserialize_DecimalAndPercentMismatch() throws IOException {
        final String inputString = "{\"percent\":\"24.0%\",\"asDouble\":0.25}";
        final InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        final JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        Assertions.assertThrows(IOException.class, () -> PercentSerializer.expansiveDeserialize(jsonParser, null));
    }

    @Test
    public void simpleDeserialize() throws IOException {
        final String input = "\"25.0%\"";
        final InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        final JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        final Percent percent = PercentSerializer.simpleDeserialize(jsonParser, null);
        Assertions.assertEquals(Percent.fromPercent(25.0), percent);
    }

    @Test
    public void deserialize() throws IOException {
        final String input = "\"25.0%\"";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        final Percent percent = objectMapper.readValue(jsonParser, Percent.class);
        Assertions.assertEquals(Percent.fromPercent(25.0), percent);
    }

    @Test
    public void deserialize_invalidInput() throws IOException {
        final String input = "\"invalid input\"";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        Assertions.assertThrows(IOException.class, () -> objectMapper.readValue(jsonParser, Percent.class));
    }

    @Test
    public void nestedDeserialize() throws IOException {
        final String input = "{\"percent\":\"25.0%\"}";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);
        final TestObject obj = objectMapper.readValue(jsonParser, TestObject.class);
        Assertions.assertEquals(Percent.fromPercent(25.0), obj.percent);
    }
}
