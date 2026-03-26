package org.finance.calcs.core.dataSerializing;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClassSerializerTuple<T> {
    final Class<T> clazz;
    final StdSerializer<? extends T> stdSerializer;
    final StdDeserializer<? extends T> stdDeserializer;
}
