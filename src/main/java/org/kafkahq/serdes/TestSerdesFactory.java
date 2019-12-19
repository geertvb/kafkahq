package org.kafkahq.serdes;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Factory
public class TestSerdesFactory {

    @Primary
    @Singleton
    public DeserializerFactory testDeserializerFactory() {
        return (String clusterId) -> {
            log.debug("Create TestDeserializer for {}", clusterId);
            return new TestDeserializer();
        };
    }

}