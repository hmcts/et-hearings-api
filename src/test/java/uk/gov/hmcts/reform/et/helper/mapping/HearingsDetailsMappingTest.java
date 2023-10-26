package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLocation;
import uk.gov.hmcts.reform.et.model.service.YesNo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class HearingsDetailsMappingTest {
    @Mock
    private CaseData caseData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAutoListFlag() {
        when(caseData.getAutoListFlag()).thenReturn(String.valueOf(YesNo.YES));
        assertTrue(HearingsDetailsMapping.getAutoListFlag(caseData), "AutoListFlag should be true");
    }

    @Test
    void testGetHearingPriorityType() {
        assertEquals(HearingsDetailsMapping.STANDARD_PRIORITY, HearingsDetailsMapping.getHearingPriorityType(),
                "Hearing priority type should match");
    }

    @Test
    void testGetNumberOfPhysicalAttendees() {
        assertEquals(0, HearingsDetailsMapping.getNumberOfPhysicalAttendees(),
                "Number of physical attendees should be 0");
    }

    @Test
    void testIsHearingInWelshFlag() {
        assertFalse(HearingsDetailsMapping.isHearingInWelshFlag(caseData),
                "HearingInWelshFlag should be false");
    }

    @Test
    void testIsPrivateHearingRequiredFlag() {
        when(caseData.getPrivateHearingRequiredFlag()).thenReturn(String.valueOf(YesNo.YES));
        assertTrue(HearingsDetailsMapping.isPrivateHearingRequiredFlag(caseData),
                "PrivateHearingRequiredFlag should be true");
    }

    @Test
    void testGetLeadJudgeContractType() {
        assertEquals("", HearingsDetailsMapping.getLeadJudgeContractType(caseData),
                "LeadJudgeContractType should be null");
    }

    @Test
    void testIsHearingIsLinkedFlag() {
        when(caseData.getHearingIsLinkedFlag()).thenReturn("Yes");
        assertTrue(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData),
                "HearingIsLinkedFlag should be true");
        when(caseData.getHearingIsLinkedFlag()).thenReturn("false");
        assertFalse(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData),
                "HearingIsLinkedFlag should be false");
    }

    @Test
    void testGetTribunalAndOfficeLocation() {
        when(caseData.getCaseManagementLocation()).thenReturn(CaseLocation.builder().baseLocation("316313").build());
        assertEquals("316313", HearingsDetailsMapping.getTribunalAndOfficeLocation(caseData),
                "TribunalAndOfficeLocation should be null");
    }
}
