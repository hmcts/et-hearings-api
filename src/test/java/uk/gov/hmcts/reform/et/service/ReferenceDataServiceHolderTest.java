package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceDataServiceHolderTest {

    @Test
    void testHmctsServiceId() {
        ReferenceDataServiceHolder holder = new ReferenceDataServiceHolder();
        holder.setHmctsServiceId("ServiceId");
        String hmctsServiceId = holder.getHmctsServiceId();

        assertEquals("ServiceId", hmctsServiceId, "hmctsServiceId should match expected value");
    }
}
