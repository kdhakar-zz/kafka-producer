package core.client;

import io.vertx.ext.web.RoutingContext;
import core.model.KafkaRecord;

import java.util.Optional;

/**
 * @author : komal.nagar
 */
public interface RequestTransformer<V> {
    /*
    Method to construct kafka record from request's routingContext.
     */
    Optional<KafkaRecord<V>> getKafkaRecord(RoutingContext routingContext);
}
