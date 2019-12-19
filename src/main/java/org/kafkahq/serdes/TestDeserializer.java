package org.kafkahq.serdes;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Base64;
import java.util.Map;

@Slf4j
public class TestDeserializer implements Deserializer {

    @Override
    public void configure(Map configs, boolean isKey) {
        // Nothing to do
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        try {
            return topic + ": " + Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            log.error("Failed to deserialize " + data, e);
            return "ERROR";
        }
    }

    @Override
    public void close() {
        // Nothing to do
    }

}
