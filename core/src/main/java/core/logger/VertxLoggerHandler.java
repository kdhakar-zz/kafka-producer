package core.logger;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author : komal.nagar
 */
public class VertxLoggerHandler implements Handler<RoutingContext> {
    private final io.vertx.core.logging.Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DateFormat dateTimeFormat;
    private static Set<String> ignoreURISet = new HashSet<>();

    public VertxLoggerHandler(Set<String> ignoreURIs) {
        dateTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss.SSS Z");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        ignoreURISet.addAll(ignoreURIs);
    }

    private String getClientAddress(SocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            return null;
        }
        return inetSocketAddress.host();
    }

    private void log(RoutingContext context, long timestamp, String remoteClient, HttpVersion version, HttpMethod method, String uri) {
        //if uri is register to ignore, do not log.
        if (ignoreURISet.contains(uri)) {
            return;
        }

        HttpServerRequest request = context.request();
        long contentLength = request.response().bytesWritten();

        String versionFormatted = "-";
        switch (version) {
            case HTTP_1_0:
                versionFormatted = "HTTP/1.0";
                break;
            case HTTP_1_1:
                versionFormatted = "HTTP/1.1";
                break;
        }

        int status = request.response().getStatusCode();
        //clientId and request Id are the client contract headers.
        String clientId = context.request().getHeader("X-Client");
        if (clientId == null) {
            clientId = "-";
        }
        String requestId = context.request().getHeader("X-Request-Id");
        if (requestId == null) {
            requestId = "-";
        }

        String message = String.format("%s %s [%s] \"%s %s %s\" %d %d %d %s",
                                       clientId,
                                       remoteClient,
                                       dateTimeFormat.format(new Date(timestamp)),
                                       method,
                                       uri,
                                       versionFormatted,
                                       status,
                                       contentLength,
                                       (System.currentTimeMillis() - timestamp),
                                       requestId);

        doLog(status, message);
    }

    private void doLog(int status, String message) {
        if (status >= 500) {
            logger.error(message);
        } else if (status >= 400) {
            logger.warn(message);
        } else {
            logger.info(message);
        }
    }

    @Override
    public void handle(RoutingContext context) {
        // common logging data
        String uri = context.request().uri();
        long timestamp = System.currentTimeMillis();
        String remoteClient = getClientAddress(context.request().remoteAddress());
        HttpMethod method = context.request().method();
        HttpVersion version = context.request().version();

        context.addBodyEndHandler(v -> log(context, timestamp, remoteClient, version, method, uri));
        context.next();
    }
}
