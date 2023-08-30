package uk.gov.hmcts.reform.et.model.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class ReferenceDataServiceHolder {

    @Value("${et.hmctsServiceId}")
    private String hmctsServiceId;

    public String getHmctsServiceId() {
        return hmctsServiceId;
    }
}

