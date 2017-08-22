package app.util;


import app.model.Constants;
import core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : komal.nagar
 */
public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static boolean isLocalEnv() throws ServiceException {
        String env = System.getProperty(Constants.ENVIRONMENT);
        logger.info("ENVIRONMENT is {}", env == null ? "Not defined" : env);
        return Constants.LOCAL_ENVIRONMENT.equalsIgnoreCase(env);
    }
}
