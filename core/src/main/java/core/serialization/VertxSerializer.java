package core.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.util.VertxUtil;

import java.util.Map;

/**
 * @author : komal.nagar
 */
public class VertxSerializer<T> implements Serializer<T> {
    private static final Logger log = LoggerFactory.getLogger(VertxSerializer.class);

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            if (data == null) {
                return new byte[0];
            }

            if (data instanceof String) {
                return ((String) data).getBytes();
            } else {
                return VertxUtil.getObjectMapper().writeValueAsBytes(data);
            }
        } catch (JsonProcessingException e) {
            log.error("Exception while serializing kafka record : {}", data, e);
            return new byte[0];
        }
    }

    @Override
    public void close() {

    }
}
