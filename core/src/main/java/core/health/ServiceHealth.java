package core.health;

import core.RequestHandler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will help if you are planning to deploy service on multiple servers behind ELB.
 *
 * @author : komal.nagar
 */
public class ServiceHealth extends RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceHealth.class);

    private static final String OOR = "oor";
    private static final String BIR = "bir";
    private static final String STATUS = "status";

    //by default keep server behind ELB as soon as service comes up.
    private static volatile boolean inRotation = true;

    /**
     * This api allows user to verify if service is up.
     */
    public void serviceHealthCheck(RoutingContext routingContext) {
        success(routingContext);
    }

    /*
    API to check service core.health.
     */
    public void elbHealthCheck(RoutingContext routingContext) {
        if (inRotation) {
            success(routingContext);
        } else {
            unavailable(routingContext);
        }
    }

    /*
    API to check/change rotation status of the service (to remove/add server behind elb).
     */
    public void changeRotationStatus(RoutingContext routingContext) {
        String command = routingContext.pathParam("command");

        switch (command) {
            case OOR:
                inRotation = false;
                success(routingContext);
                break;
            case BIR:
                inRotation = true;
                success(routingContext);
                break;
            case STATUS:
                serviceHealthCheck(routingContext);
                break;
            default:
                log.error("Rotation command not supported");
                badRequest(routingContext);
        }
    }
}
