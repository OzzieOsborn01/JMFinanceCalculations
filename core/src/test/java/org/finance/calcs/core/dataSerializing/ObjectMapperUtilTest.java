package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.core.JsonParser;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectMapperUtilTest {
   private static ObjectMapperUtil objectMapperUtil;

   @BeforeAll
   public static void setup() {
       objectMapperUtil = ObjectMapperUtil.getInstance();
   }

    @Test
    public void serialize() throws IOException {
        final Percent percent = Percent.fromPercent(25.0);
        final String actual = objectMapperUtil.getObjectMapper().writeValueAsString(percent);
        Assertions.assertEquals("\"25.0%\"", actual);
    }

    @Test
    public void deserialize() throws IOException {
        final String input = "\"25.0%\"";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JsonParser jsonParser = objectMapperUtil.getObjectMapper().getFactory().createParser(inputStream);
        final Percent percent = objectMapperUtil.getObjectMapper().readValue(jsonParser, Percent.class);
        Assertions.assertEquals(Percent.fromPercent(25.0), percent);
    }
}
