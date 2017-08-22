package core;

import io.vertx.ext.web.RoutingContext;
import core.metrics.MetricManager;
import org.apache.commons.httpclient.HttpStatus;

/**
 * @author : komal.nagar
 */
public abstract class RequestHandler {
    public void success(RoutingContext routingContext) {
        publishMetric(routingContext, HttpStatus.SC_OK);
        routingContext.response().setStatusCode(HttpStatus.SC_OK).end();
    }

    public void fail(RoutingContext routingContext) {
        publishMetric(routingContext, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        routingContext.response().setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR).end();
    }

    public void unavailable(RoutingContext routingContext) {
        routingContext.response().setStatusCode(HttpStatus.SC_SERVICE_UNAVAILABLE).end();
    }

    public void badRequest(RoutingContext routingContext) {
        publishMetric(routingContext, HttpStatus.SC_BAD_REQUEST);
        routingContext.response().setStatusCode(HttpStatus.SC_BAD_REQUEST).end();
    }

    void publishMetric(RoutingContext routingContext, int httpStatus) {
        MetricManager.markMeter(routingContext.request().uri().substring(1) + httpStatus);
    }
}
