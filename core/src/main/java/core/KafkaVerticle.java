package core;

import core.client.HttpKafkaProducer;
import core.client.KafkaHandler;
import core.client.ServiceConfig;
import core.client.VertxConfig;
import core.exception.ServiceException;
import core.health.ServiceHealth;
import core.logger.VertxLoggerHandler;
import core.model.HandlerProperty;
import core.model.VertxConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author : komal.nagar
 */
public class KafkaVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(KafkaVerticle.class);

    private ServiceConfig serviceConfig;
    private VertxConfig vertxConfig;

    /**
     * Method to create Vertx.
     */
    protected void buildVertx(VertxConfig vertxConfig, ServiceConfig serviceConfig) throws ServiceException {
        this.serviceConfig = serviceConfig;
        this.vertxConfig = vertxConfig;
        registerLog();

        VertxOptions vertxOptions = vertxConfig.getVertxOptions();
        log.info("creating vertx with options : {}", vertxOptions);

        Consumer<Vertx> vertxConsumer = vertxRunner -> vertxRunner.deployVerticle(this);
        Vertx vertx = Vertx.vertx(vertxOptions);
        vertxConsumer.accept(vertx);
    }

    /**
     * Startup method which will will load routers and create http server.
     *
     * @throws ServiceException : service core.exception from config provider.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void start() throws ServiceException {
        log.info("starting vertx application ...");

        //router and handlers for http request url.
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(new VertxLoggerHandler(serviceConfig.getURLToIgnore()));

        //service health check and rotation status api.
        ServiceHealth serviceHealth = new ServiceHealth();
        router.get(VertxConstants.SERVICE_HEALTH).handler(serviceHealth::serviceHealthCheck);
        router.get(VertxConstants.ELB_HEALTH_CHECK).handler(serviceHealth::elbHealthCheck);
        router.put(VertxConstants.ROTATION_STATUS).handler(serviceHealth::changeRotationStatus);

        //build kafka producers.
        List<HandlerProperty> handlerPropertyList = vertxConfig.getHandlerProperties();
        for (HandlerProperty property : handlerPropertyList) {
            KafkaProducer kafkaProducer = new HttpKafkaProducer().create(property, vertx);
            router.put(property.getHttpURL()).handler(new KafkaHandler(serviceConfig, kafkaProducer, property.getRequestTransformer())::produceEvents);
            log.info("registered URL : {} to process kafka record", property.getHttpURL());
        }

        //create httpServer with request handler
        int servicePort = vertxConfig.getServicePort();
        vertx.createHttpServer().requestHandler(router::accept).listen(servicePort);
        log.info("service started on port : {}", servicePort);
    }

    /*
    Method to register log, use slf4j implementation for access {http request} logs.
     */
    private void registerLog() {
        System.setProperty(VertxConstants.VERTX_LOGGER_DELEGATE_FACTORY_CLASS_KEY, VertxConstants.VERTX_LOGGER_DELEGATE_FACTORY_CLASS_VALUE);
    }
}
