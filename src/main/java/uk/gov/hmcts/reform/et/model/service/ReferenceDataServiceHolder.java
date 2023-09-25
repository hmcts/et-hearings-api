package uk.gov.hmcts.reform.et.model.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class ReferenceDataServiceHolder {

    public static final String DEFAULT_CATEGORY = "Employment";

    @Value("${et.hmctsServiceId}")
    private String hmctsServiceId;

    @Value(DEFAULT_CATEGORY)
    private String categoryType;

    @Value(DEFAULT_CATEGORY)
    private String categoryValue;

    @Value(DEFAULT_CATEGORY)
    private String categoryParent;

    public String getHmctsServiceId() {
        return hmctsServiceId;
    }

}

