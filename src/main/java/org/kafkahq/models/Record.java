package org.kafkahq.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.record.TimestampType;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
public class Record {
    private final String topic;
    private final int partition;
    private final long offset;
    private final long timestamp;
    private final TimestampType timestampType;
    private final byte[] key;
    private final Integer keySchemaId;
    private final byte[] value;
    private final Integer valueSchemaId;
    private final Map<String, String> headers = new HashMap<>();
    private final Deserializer valueDeserializer;
    private final Deserializer keyDeserializer;

    public Record(ConsumerRecord<byte[], byte[]> record, Deserializer keyDeserializer, Deserializer valueDeserializer) {
        this.topic = record.topic();
        this.partition = record.partition();
        this.offset = record.offset();
        this.timestamp = record.timestamp();
        this.timestampType = record.timestampType();
        this.key = record.key();
        this.keySchemaId = getAvroSchemaId(this.key);
        this.value = record.value();
        this.valueSchemaId = getAvroSchemaId(this.value);
        for (Header header : record.headers()) {
            this.headers.put(header.key(), header.value() != null ? new String(header.value()) : null);
        }

        this.valueDeserializer = valueDeserializer;
        this.keyDeserializer = keyDeserializer;
    }

    public String getKeyAsString() {
        return convertToString(key, keySchemaId, keyDeserializer);
    }

    public String getKeyAsBase64() {
        if (key == null) {
            return null;
        } else {
            return new String(Base64.getEncoder().encode(key));
        }
    }

    public String getValueAsString() {
        return convertToString(value, valueSchemaId, valueDeserializer);
    }

    private String convertToString(byte[] payload, Integer schemaId, Deserializer deserializer) {
        if (payload == null) {
            return null;
        } else /*if (schemaId != null)*/ {
            try {
                Object deserialize = deserializer.deserialize(topic, payload);
                return deserialize.toString();
            } catch (Exception exception) {
                return new String(payload);
            }
//        } else {
//            return new String(payload);
        }
    }

    private static Integer getAvroSchemaId(byte[] payload) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(payload);
            byte magicBytes = buffer.get();
            int schemaId = buffer.getInt();

            if (magicBytes == 0 && schemaId >= 0) {
                return schemaId;
            }
        } catch (Exception ignore) {

        }
        return null;
    }
}
