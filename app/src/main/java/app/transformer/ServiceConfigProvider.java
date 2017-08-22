package app.transformer;

import core.client.ServiceConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : komal.nagar
 */
public class ServiceConfigProvider implements ServiceConfig {

    @Override
    public Set<String> getURLToIgnore() {
        //set of url's to ignore from access/application logs
        return new HashSet<>();
    }

    @Override
    public Set<String> getTopicSetToDropEvent() {
        //set of topics to drop events for
        return new HashSet<>();
    }
}
