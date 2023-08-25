package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServiceHearingValuesMappingTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnServiceHearingValues() throws IOException, URISyntaxException {
        ServiceHearingValues mockServiceHearingValues = new CaseTestData().expectedServiceHearingValues();

        assertFalse(mockServiceHearingValues.isAutoListFlag(), "is auto list flag");
        assertEquals("Johnny Claimant v Acme Redde Ltd", mockServiceHearingValues.getPublicCaseName(),
                "get public case name");
        assertEquals("/documents/deep/link", mockServiceHearingValues.getCaseDeepLink(),
                "get case deep link");
        assertEquals("Johnny Claimant v Acme Redde Ltd", mockServiceHearingValues.getCaseNameHmctsInternal(),
                "hmcts internal case name");
        assertNull(mockServiceHearingValues.getReceiptDate(), "case SLA start date");
        assertFalse(mockServiceHearingValues.isCaseRestrictedFlag(), "case restricted flag");
        assertFalse(mockServiceHearingValues.isAutoListFlag(), "auto list flag");
        assertEquals("Hearing", mockServiceHearingValues.getHearingType(), "hearing type");
        assertEquals(45, mockServiceHearingValues.getDuration(), "duration");
        assertEquals("Standard", mockServiceHearingValues.getHearingPriorityType(),
                "hearing priority type");
        assertEquals(0, mockServiceHearingValues.getNumberOfPhysicalAttendees(),
                "no. of physical attendees");
        assertFalse(mockServiceHearingValues.isHearingInWelshFlag(), "hearing in Welsh flag");
        assertFalse(mockServiceHearingValues.getCaseAdditionalSecurityFlag(), "additional security flag");
        assertFalse(mockServiceHearingValues.isPrivateHearingRequiredFlag(), "private hearing required");
        assertEquals("", mockServiceHearingValues.getLeadJudgeContractType(), "lead judge contract");
        assertFalse(mockServiceHearingValues.isHearingIsLinkedFlag(), "is hearing linked");
        assertEquals("BHA1", mockServiceHearingValues.getHmctsServiceId(), "HMCTS service id");
    }
}
