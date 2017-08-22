package core.client;

import core.exception.ServiceException;
import core.model.HandlerProperty;
import io.vertx.core.VertxOptions;

import java.util.List;

/**
 * @author : komal.nagar
 */
public interface VertxConfig {
    /*
    Vertx options like worker thread pool and core.metrics.
     */
    VertxOptions getVertxOptions() throws ServiceException;

    /*
    Http service port.
     */
    int getServicePort() throws ServiceException;

    /*
    User has to set following configs.
    1. properties: below four properties are must but user can provide additional configs according to use case.
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
        ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG {can use StringSerializer.class directly}
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG  {can use VertxSerializer.class directly}
    2. httpURL
    3. transformer implementation : Kafka producer value transformer which will transform request body of http request into kafka record value.
     */
    List<HandlerProperty> getHandlerProperties() throws ServiceException;
}
