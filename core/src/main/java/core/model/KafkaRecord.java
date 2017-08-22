package core.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author : komal.nagar
 */
public class KafkaRecord<V> {
    private String topic;
    private V value;

    public KafkaRecord(String topic, V value) {
        this.topic = topic;
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("topic", topic)
                .append("value", value)
                .toString();
    }
}
