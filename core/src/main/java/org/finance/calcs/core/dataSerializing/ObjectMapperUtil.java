package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.util.Arrays;
import java.util.List;

public class ObjectMapperUtil {
    private static ObjectMapperUtil instance; // Private static instance

    private ObjectMapper objectMapper;

    private List<ClassSerializerTuple> serializerTuples;

    private ObjectMapperUtil() { // Private constructor
        serializerTuples = Arrays.asList(
                new PercentSerializer().getClassSerializerTuple()
        );

        SimpleModule simpleModule = new SimpleModule();
        serializerTuples.forEach(tuple -> {
            if (tuple.getStdDeserializer() != null)
                simpleModule.addDeserializer(tuple.getClazz(), tuple.getStdDeserializer());
            if (tuple.getStdSerializer() != null)
                simpleModule.addSerializer(tuple.getClazz(), tuple.getStdSerializer());
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
}
