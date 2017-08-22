package core.client;

import core.model.HandlerProperty;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : komal.nagar
 */
public class HttpKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(HttpKafkaProducer.class);

    /**
     * Create a new KafkaProducer instance
     *
     * @param handlerProperty kafka property having configs
     * @param vertx           Vert.x instance to use
     * @return an instance of the KafkaProducer
     */
    public <K, V> KafkaProducer create(HandlerProperty handlerProperty, Vertx vertx) {
        KafkaProducer<K, V> producer = KafkaProducer.create(vertx, handlerProperty.getConfig());
        if (handlerProperty.getWriteQueueMaxSize() != 0) {
            producer.setWriteQueueMaxSize(handlerProperty.getWriteQueueMaxSize());
        }
        log.info("kafka producer created successfully with configs : {}", handlerProperty.getConfig());
        return producer;
    }
}
