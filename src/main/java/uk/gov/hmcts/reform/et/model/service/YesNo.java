package uk.gov.hmcts.reform.et.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ecm.common.model.helper.Constants;

import static java.util.Objects.nonNull;

public enum YesNo {

    @JsonProperty(Constants.YES)
    YES(Constants.YES),
    @JsonProperty(Constants.NO)
    NO(Constants.NO);

    private final String value;

    YesNo(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static boolean isYes(YesNo yesNo) {
        return YES.equals(yesNo);
    }

    public static boolean isYes(String yesNo) {
        return nonNull(yesNo) && YES.getValue().equalsIgnoreCase(yesNo);
    }

    @Override
    public String toString() {
        return value;
    }
}

