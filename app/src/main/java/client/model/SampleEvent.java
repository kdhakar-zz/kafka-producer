package client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author : komal.nagar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleEvent implements Serializable {
    @NotNull
    private String field1;

    @NotNull
    private String field2;

    //and so on.... all required fields


    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("field1", field1)
                .append("field2", field2)
                .toString();
    }
}
