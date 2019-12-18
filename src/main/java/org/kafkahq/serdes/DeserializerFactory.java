package org.kafkahq.serdes;

import org.apache.kafka.common.serialization.Deserializer;

@FunctionalInterface
public interface DeserializerFactory {

    Deserializer getDeserializer(String clusterId);

}