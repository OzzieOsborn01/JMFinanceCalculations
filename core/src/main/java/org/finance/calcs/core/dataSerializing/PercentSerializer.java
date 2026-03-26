package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.finance.calcs.core.percent.Percent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serializer to convert {@link Percent} into a JSON object
 */
public class PercentSerializer {
    private static final String PERCENT_DECIMAL_REGEX = "([-+]?\\d*\\.?\\d*)";
    private static final String PERCENT_REGEX = PERCENT_DECIMAL_REGEX + "%";
    private static final Pattern PERCENT_DECIMAL_PATTERN = Pattern.compile(PERCENT_DECIMAL_REGEX);
    private static final Pattern PERCENT_PATTERN = Pattern.compile(PERCENT_REGEX);

    private static final String PERCENT_KEY = "percent";
    private static final String DECIMAL_KEY = "asDouble";

    private final Serializer serializer;
    private final Deserializer deserializer;

    public ClassSerializerTuple getClassSerializerTuple() {
        return new ClassSerializerTuple(Percent.class, serializer, deserializer);
    }

    public PercentSerializer() {
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    public PercentSerializer(Class<Percent> t) {
        this.serializer = new Serializer(t);
        this.deserializer = new Deserializer(t);
    }

    public static class Serializer extends StdSerializer<Percent> {
        public Serializer() {
            this(null);
        }

        public Serializer(Class<Percent> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            PercentSerializer.serialize(percent, jsonGenerator, serializer);
        }

    }
    public static class Deserializer extends StdDeserializer<Percent> {
        public Deserializer() {
            this(null);
        }

        public Deserializer(Class<Percent> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Percent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return PercentSerializer.deserialize(jsonParser, deserializationContext);
        }
    }

    /**
     * Default serialize operation
     */
    public static void serialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        simpleSerialize(percent, jsonGenerator, serializer);
    }

    /**
     * Expansive Serialization that will display both percent ("17.95%") and as double values (0.1795)
     */
    public static void expansiveSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(PERCENT_KEY, percent.toString());
        jsonGenerator.writeNumberField(DECIMAL_KEY, percent.asDouble());
        jsonGenerator.writeEndObject();
    }

    /**
     * Simple serialize function that outputs the percent as a percent string "19.75%"
     */
    public static void simpleSerialize(Percent percent, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeString(percent.toString());
    }

    /**
     * Default deserialize operation
     */
    public static Percent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return simpleDeserialize(jsonParser, deserializationContext);
    }

    /**
     * Expansive deserialization that will convert both percent ("17.95%") and as double values (0.1795) into a singular percent
     */
    public static Percent expansiveDeserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final String percentString = node.get(PERCENT_KEY).asText();
        final JsonNode decimalNode = node.get(DECIMAL_KEY);
        final Double decimal = decimalNode.isDouble() ? decimalNode.asDouble() : null;
        final Percent percent = deserializePercentString(percentString);
        if (decimal == null) {
            return percent;
        } else if (percent.compareTo(decimal) != 0) {
            throw new IOException("Percent and asDouble values do not match");
        }

        return percent;
    }

    /**
     * Simple deserialize function that outputs the percent from a percent string "19.75%"
     */
    public static Percent simpleDeserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException
    {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final String nodeStr = node.asText();

        return deserializePercentString(nodeStr);
    }

    public static Percent deserializePercentString(final String percentString)
            throws IOException
    {
        final Matcher percentMatcher = PERCENT_PATTERN.matcher(percentString);
        percentMatcher.find();
        if (!percentMatcher.hasMatch()) {
            throw new IOException("Percent value does not match expected pattern");
        }

        final Matcher decimalMatcher = PERCENT_DECIMAL_PATTERN.matcher(percentString);
        decimalMatcher.find();
        final Double decimal = Double.parseDouble(decimalMatcher.group());
        return Percent.fromPercent(decimal, 5);
    }
}
