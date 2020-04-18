package org.akhq.serdes;

import io.micronaut.context.annotation.Factory;
import org.apache.kafka.common.serialization.Deserializer;
import org.akhq.repositories.SchemaRegistryRepository;

import javax.inject.Singleton;
import java.util.function.Function;

@Factory
public class SerdesFactory {

    @Singleton
    public Function<String, Deserializer> avroDeserializerFactory(SchemaRegistryRepository schemaRegistryRepository) {
        return schemaRegistryRepository::getKafkaAvroDeserializer;
    }

}