package core.client;

import java.util.Set;

/**
 * @author : komal.nagar
 */
public interface ServiceConfig {
    /*
    to ignore set of api log.
     */
    Set<String> getURLToIgnore();

    /*
    Set of topics to drop events for.
     */
    Set<String> getTopicSetToDropEvent();
}
