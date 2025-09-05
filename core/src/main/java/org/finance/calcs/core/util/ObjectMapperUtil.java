package org.finance.calcs.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.AllArgsConstructor;
import org.finance.calcs.core.dataSerializing.PercentSerializer;
import org.finance.calcs.core.percent.Percent;

import java.util.Arrays;
import java.util.List;

public class ObjectMapperUtil {
    private static ObjectMapperUtil instance; // Private static instance

    private ObjectMapper objectMapper;

    private List<ClassSerializerTuple> serializerTuples;

    private ObjectMapperUtil() { // Private constructor
        serializerTuples = Arrays.asList(
                new ClassSerializerTuple(Percent.class, new PercentSerializer(), null)
        );

        SimpleModule simpleModule = new SimpleModule();
        serializerTuples.forEach(tuple -> {
            if (tuple.stdDeserializer != null)
                simpleModule.addDeserializer(tuple.clazz, tuple.stdDeserializer);
            if (tuple.stdSerializer != null)
                simpleModule.addSerializer(tuple.clazz, tuple.stdSerializer);
        });

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(new Jdk8Module());
    }

    public static ObjectMapperUtil getInstance() { // Public static factory method
        if (instance == null) {
            instance = new ObjectMapperUtil();
        }
        return instance;
    }

    // Other methods of the Singleton class
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @AllArgsConstructor
    private class ClassSerializerTuple<T> {
        final Class<T> clazz;
        final StdSerializer<? extends T> stdSerializer;
        final StdDeserializer<? extends T> stdDeserializer;
    }
}
