package core.model;

/**
 * @author : komal.nagar
 */
public class VertxConstants {
    public static final String ELB_HEALTH_CHECK = "/elb-healthcheck";
    public static final String ROTATION_STATUS = "/rotation/:command";
    public static final String VERTX_LOGGER_DELEGATE_FACTORY_CLASS_KEY = "vertx.core.logger-delegate-factory-class-name";
    public static final String VERTX_LOGGER_DELEGATE_FACTORY_CLASS_VALUE = "io.vertx.core.logging.SLF4JLogDelegateFactory";
    public static final String SERVICE_HEALTH = "/healthcheck";
}
