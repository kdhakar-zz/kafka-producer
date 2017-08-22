package core.metrics;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * @author : komal.nagar
 */
public class MetricManager {
    private static final MetricRegistry metricRegistry = new MetricRegistry();

    public static void register() {
        JmxReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS).build().start();
    }

    public static void markMeter(String metricName) {
        metricRegistry.meter(metricName).mark();
    }
}
