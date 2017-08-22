package client;

import client.transformer.ServiceConfigProvider;
import client.transformer.VertxConfigProvider;
import core.KafkaVerticle;
import core.exception.ServiceException;
import core.metrics.MetricManager;

/**
 * @author : komal.nagar
 */
public class Application extends KafkaVerticle {

    public static void main(String[] args) throws ServiceException {
        MetricManager.register();
        new Application().provideConfigAndBuildVertx();
    }

    private void provideConfigAndBuildVertx() throws ServiceException {
        super.buildVertx(new VertxConfigProvider(), new ServiceConfigProvider());
    }
}
