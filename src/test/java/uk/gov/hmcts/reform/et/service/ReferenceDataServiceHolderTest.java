package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceDataServiceHolderTest {

    private ReferenceDataServiceHolder holder;

    @BeforeEach
    void setUp() {
        holder = new ReferenceDataServiceHolder();
    }

    @Test
    void testHmctsServiceId() {
        String expectedServiceId = "ServiceId";
        holder.setHmctsServiceId(expectedServiceId);
        String hmctsServiceId = holder.getHmctsServiceId();
        assertEquals(expectedServiceId, hmctsServiceId, "hmctsServiceId should match the expected value");
    }
}
