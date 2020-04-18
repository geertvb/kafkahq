package org.akhq.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.*;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
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
@NoArgsConstructor
public class Record {
    private String topic;
    private int partition;
    private long offset;
    private long timestamp;
    private TimestampType timestampType;
    private Integer keySchemaId;
    private Integer valueSchemaId;
    private Map<String, String> headers = new HashMap<>();
    private Deserializer valueDeserializer;
    private Deserializer keyDeserializer;

    @Getter(AccessLevel.NONE)
    private byte[] bytesKey;

    @Getter(AccessLevel.NONE)
    private String key;

    @Getter(AccessLevel.NONE)
    private byte[] bytesValue;

    @Getter(AccessLevel.NONE)
    private String value;

    public Record(RecordMetadata record, byte[] bytesKey, byte[] bytesValue, Map<String, String> headers) {
        this.topic = record.topic();
        this.partition = record.partition();
        this.offset = record.offset();
        this.timestamp = record.timestamp();
        this.bytesKey = bytesKey;
        this.keySchemaId = getAvroSchemaId(this.bytesKey);
        this.bytesValue = bytesValue;
        this.valueSchemaId = getAvroSchemaId(this.bytesValue);
        this.headers = headers;
        this.valueDeserializer = null;
        this.keyDeserializer = null;
    }

    public Record(ConsumerRecord<byte[], byte[]> record, Deserializer keyDeserializer, Deserializer valueDeserializer, byte[] bytesValue) {
        this.topic = record.topic();
        this.partition = record.partition();
        this.offset = record.offset();
        this.timestamp = record.timestamp();
        this.timestampType = record.timestampType();
        this.bytesKey = record.key();
        this.keySchemaId = getAvroSchemaId(this.bytesKey);
        this.bytesValue = bytesValue;
        this.valueSchemaId = getAvroSchemaId(this.bytesValue);
        for (Header header : record.headers()) {
            this.headers.put(header.key(), header.value() != null ? new String(header.value()) : null);
        }

        this.valueDeserializer = valueDeserializer;
        this.keyDeserializer = keyDeserializer;
    }

    public String getKey() {
        if (this.key == null) {
            this.key = convertToString(bytesKey, keySchemaId, keyDeserializer);
        }

        return this.key;
    }

    @JsonIgnore
    public String getKeyAsBase64() {
        if (bytesKey == null) {
            return null;
        } else {
            return new String(Base64.getEncoder().encode(bytesKey));
        }
    }

    public String getValue() {
        if (this.value == null) {
            this.value = convertToString(bytesValue, valueSchemaId, valueDeserializer);
        }

        return this.value;
    }

    private String convertToString(byte[] payload, Integer schemaId, Deserializer deserializer) {
        if (payload == null) {
            return null;
        } else /* if (keySchemaId != null) */ {
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
