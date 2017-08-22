package core.model;

import core.client.RequestTransformer;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Properties;

/**
 * @author : komal.nagar
 */
public class HandlerProperty<V> {
    private Properties config;
    private String httpURL;
    //default value (1024*1024) will be used of use doesn't set this (keep 0).
    private int writeQueueMaxSize;
    private RequestTransformer<V> requestTransformer;

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public String getHttpURL() {
        return httpURL;
    }

    public void setHttpURL(String httpURL) {
        this.httpURL = httpURL;
    }

    public int getWriteQueueMaxSize() {
        return writeQueueMaxSize;
    }

    public void setWriteQueueMaxSize(int writeQueueMaxSize) {
        this.writeQueueMaxSize = writeQueueMaxSize;
    }

    public RequestTransformer<V> getRequestTransformer() {
        return requestTransformer;
    }

    public void setRequestTransformer(RequestTransformer<V> requestTransformer) {
        this.requestTransformer = requestTransformer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("config", config)
                .append("httpURL", httpURL)
                .append("writeQueueMaxSize", writeQueueMaxSize)
                .append("requestTransformer", requestTransformer)
                .toString();
    }
}
