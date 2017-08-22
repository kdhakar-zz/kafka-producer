package client.transformer;

import client.model.SampleEvent;
import core.client.VertxConfig;
import core.exception.ServiceException;
import core.model.HandlerProperty;
import core.serialization.VertxSerializer;
import io.vertx.core.VertxOptions;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * this is the sample vertx config.
 *
 * @author : komal.nagar
 */
public class VertxConfigProvider implements VertxConfig {
    private static final String EVENT_URL = "/event";

    @Override
    public VertxOptions getVertxOptions() throws ServiceException {
        VertxOptions options = new VertxOptions();
        //This config will produce warnings only and will not terminate request.
        int maxEventLoopExecutionTime = 200;// In ms.
        options.setMaxEventLoopExecuteTime(1000000 * maxEventLoopExecutionTime);//in ns
        return options;
    }

    @Override
    public int getServicePort() throws ServiceException {
        return 1234;
    }

    /*
     Method to provide multiple api url and corresponding configs to process
      different type of events and write to corresponding (config driven) kafka cluster.
     */
    @Override
    public List<HandlerProperty> getHandlerProperties() throws ServiceException {
        List<HandlerProperty> handlerPropertyList = new ArrayList<>();
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ip:port,ip:port .....");
        //config to define how long producer will be blocked if buffer is full or metadata unavailable (topic is not created).
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1000);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VertxSerializer.class);

        //1. handler config for
        HandlerProperty<SampleEvent> samplEvent = new HandlerProperty<>();
        samplEvent.setConfig(properties);
        samplEvent.setHttpURL(EVENT_URL);
        samplEvent.setRequestTransformer(new SampleTransformer());
        handlerPropertyList.add(samplEvent);

        //2. handler config for .... other event

        return handlerPropertyList;
    }
}
