package uk.gov.hmcts.reform.et.model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Objects.nonNull;

public enum YesNo {

    @JsonProperty("Yes")
    YES("Yes"),
    @JsonProperty("No")
    NO("No");

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

