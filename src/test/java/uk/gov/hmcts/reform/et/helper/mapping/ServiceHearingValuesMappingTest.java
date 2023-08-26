package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ServiceHearingValuesMappingTest {

    public static final String PUBLIC_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";
    public static final String HMCTS_INTERNAL_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";

    @Mock
    private CaseData caseData;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnServiceHearingValues() throws IOException, URISyntaxException {

        when(caseData.getAutoListFlag()).thenReturn(String.valueOf(false));
        when(caseData.getPublicCaseName()).thenReturn(PUBLIC_CASE_NAME);
        when(caseData.getCaseDeepLink()).thenReturn("/documents/deep/link");
        when(caseData.getCaseNameHmctsInternal()).thenReturn(HMCTS_INTERNAL_CASE_NAME);
        when(caseData.getReceiptDate()).thenReturn(null);
        when(caseData.getCaseRestrictedFlag()).thenReturn(String.valueOf(false));
        when(caseData.getHearingType()).thenReturn("Hearing");
        when(caseData.getDuration()).thenReturn(45);
        when(caseData.getHearingPriorityType()).thenReturn("Standard");
        when(caseData.getNumberOfPhysicalAttendees()).thenReturn(0);
        when(caseData.getHearingInWelshFlag()).thenReturn(String.valueOf(false));
        when(caseData.getCaseAdditionalSecurityFlag()).thenReturn(String.valueOf(false));
        when(caseData.getPrivateHearingRequiredFlag()).thenReturn(String.valueOf(false));
        when(caseData.getLeadJudgeContractType()).thenReturn("");
        when(caseData.getHearingIsLinkedFlag()).thenReturn(String.valueOf(false));
        when(caseData.getHmctsServiceID()).thenReturn("BHA1");

        ServiceHearingValues mockServiceHearingValues = new CaseTestData().expectedServiceHearingValues();

        assertFalse(mockServiceHearingValues.isAutoListFlag(), "is auto list flag");
        assertEquals(PUBLIC_CASE_NAME, mockServiceHearingValues.getPublicCaseName(),
                "get public case name");
        assertEquals("/documents/deep/link", mockServiceHearingValues.getCaseDeepLink(),
                "get case deep link");
        assertEquals(HMCTS_INTERNAL_CASE_NAME, mockServiceHearingValues.getCaseNameHmctsInternal(),
                "hmcts internal case name");
        assertNull(mockServiceHearingValues.getReceiptDate(), "case SLA start date");
        assertFalse(mockServiceHearingValues.isCaseRestrictedFlag(), "case restricted flag");
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
