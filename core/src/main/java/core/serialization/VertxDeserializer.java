package core.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.util.VertxUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author : komal.nagar
 */
public class VertxDeserializer<T> implements Deserializer {
    private static final Logger log = LoggerFactory.getLogger(VertxDeserializer.class);

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return VertxUtil.getObjectMapper().readValue(data, new TypeReference<T>() {
            });
        } catch (IOException e) {
            log.error("Exception while reading value from byte array {}", Arrays.toString(data), e);
            return null;
        }
    }

    @Override
    public void close() {

    }
}
