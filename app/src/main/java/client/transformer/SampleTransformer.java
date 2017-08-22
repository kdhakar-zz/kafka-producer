package client.transformer;

import client.model.SampleEvent;
import client.model.Constants;
import core.client.RequestTransformer;
import core.model.KafkaRecord;
import core.util.VertxUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

/**
 * @author : komal.nagar
 */
public class SampleTransformer implements RequestTransformer<SampleEvent> {

    @Override
    public Optional<KafkaRecord<SampleEvent>> getKafkaRecord(RoutingContext routingContext) {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        SampleEvent sampleEvent = VertxUtil.getObjectMapper().convertValue(jsonObject.getMap(), SampleEvent.class);
        return Optional.of(new KafkaRecord(Constants.UPDATE_TOPIC, sampleEvent));
    }
}
