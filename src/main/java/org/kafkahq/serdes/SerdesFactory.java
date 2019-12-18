package org.kafkahq.serdes;

import io.micronaut.context.annotation.Factory;
import org.kafkahq.repositories.SchemaRegistryRepository;

import javax.inject.Singleton;

@Factory
public class SerdesFactory {

    @Singleton
    public DeserializerFactory avroDeserializerFactory(SchemaRegistryRepository schemaRegistryRepository) {
        return schemaRegistryRepository::getKafkaAvroDeserializer;
    }

}