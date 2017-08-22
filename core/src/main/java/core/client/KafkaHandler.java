package core.client;

import core.RequestHandler;
import core.model.KafkaRecord;
import io.vertx.ext.web.RoutingContext;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author : komal.nagar
 */
public class KafkaHandler<K, V> extends RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(KafkaHandler.class);

    private ServiceConfig serviceConfig;
    private KafkaProducer<K, V> producer;
    private RequestTransformer<V> requestTransformer;

    public KafkaHandler(ServiceConfig serviceConfig, KafkaProducer<K, V> producer, RequestTransformer<V> requestTransformer) {
        this.serviceConfig = serviceConfig;
        this.producer = producer;
        this.requestTransformer = requestTransformer;
    }

    /**
     * Method to convert jsonObject of routingContext to kafkaRecord and
     * write kafkaRecord on kafka write stream.
     *
     * @param routingContext routingContext of vertx http request.
     */
    public void produceEvents(RoutingContext routingContext) {
        Optional<KafkaRecord<V>> record = requestTransformer.getKafkaRecord(routingContext);

        if (!record.isPresent() || record.get().getValue() == null) {
            log.error("invalid kafka record value : {}", routingContext.getBodyAsJson());
            badRequest(routingContext);
            return;
        }

        V recordValue = record.get().getValue();
        //if value is Iterable, write individual entries to kafka.
        if (recordValue instanceof Iterable) {
            if (((Collection<?>) recordValue).size() == 0) {
                success(routingContext);
                return;
            }
            @SuppressWarnings("unchecked") Iterator<V> iterator = ((Iterable<V>) recordValue).iterator();
            while (iterator.hasNext()) {
                writeToKafka(routingContext, record.get().getTopic(), iterator.next(), !iterator.hasNext());
            }
        } else {
            writeToKafka(routingContext, record.get().getTopic(), record.get().getValue(), true);
        }
    }

    /**
     * Internal method to write records to kafka write stream.
     * Method will close request with proper httpStatus code if it is working on last iteration of data.
     *
     * @param routingContext  routingContext of vertx http request.
     * @param topic           kafkaTopic
     * @param value           kafka record value
     * @param isLastIteration boolean variable true if it is last iteration of data writing into kafka.
     */
    private void writeToKafka(RoutingContext routingContext, String topic, V value, boolean isLastIteration) {
        KafkaProducerRecord<K, V> kafkaProducerRecord = KafkaProducerRecord.create(topic, null, value, null);

        if (serviceConfig.getTopicSetToDropEvent().contains(topic)) {
            log.info("dropping events : {}", kafkaProducerRecord.record());
            success(routingContext);
            return;
        }

        producer.write(kafkaProducerRecord, handler -> {
            if (handler.succeeded()) {
                RecordMetadata result = handler.result();
                log.info("Message written to kafka : {} with offset : {}", kafkaProducerRecord.record(), result.getOffset());
                if (isLastIteration) {
                    success(routingContext);
                }
            } else {
                log.error("Error while writing message : {}, cause : {}", kafkaProducerRecord.record(), handler.cause());
                fail(routingContext);
            }
        });
    }
}
