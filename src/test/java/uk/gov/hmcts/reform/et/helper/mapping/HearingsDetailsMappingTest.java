package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.HearingType;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.HearingDurationCalculator;
import uk.gov.hmcts.reform.et.model.service.YesNo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class HearingsDetailsMappingTest {

    public static final String REQUEST_1 = "request1";
    public static final String REQUEST_2 = "request2";
    public static final String TYPE_1 = "type1";
    public static final String TYPE_2 = "type2";

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
    void testGetHearingDuration() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();
        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(REQUEST_1);

        final HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("2");
        hearingValue.setHearingEstLengthNumType("Days");
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        int duration = HearingDurationCalculator.calculateHearingDuration(
                caseDetails,
                REQUEST_1,
                hearingCollection
        );
        assertEquals(2 * 360, duration, "Hearing duration in days should match");
    }

    @Test
    void testGetHearingEstLengthNumType() {
        when(caseData.getHearingEstLengthNumType()).thenReturn("length_type");
        assertEquals("length_type", HearingsDetailsMapping.getHearingEstLengthNumType(
                caseData), "HearingEstLengthNumType should match");
    }

    @Test
    void testGetHearingType() {
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        HearingTypeItem hearingItem1 = new HearingTypeItem();
        hearingItem1.setId(REQUEST_1);

        HearingType hearingValue1 = new HearingType();
        hearingValue1.setHearingType(TYPE_1);
        hearingItem1.setValue(hearingValue1);

        HearingTypeItem hearingItem2 = new HearingTypeItem();
        hearingItem2.setId(REQUEST_2);

        HearingType hearingValue2 = new HearingType();
        hearingValue2.setHearingType(TYPE_2);
        hearingItem2.setValue(hearingValue2);

        hearingCollection.add(hearingItem1);
        hearingCollection.add(hearingItem2);

        String hearingType = HearingsDetailsMapping.getHearingType(REQUEST_1, hearingCollection);
        assertEquals(TYPE_1, hearingType, "HearingType should match");
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
        assertNull(HearingsDetailsMapping.getLeadJudgeContractType(caseData),
                "LeadJudgeContractType should be null");
    }

    @Test
    void testIsHearingIsLinkedFlag() {
        when(caseData.getHearingIsLinkedFlag()).thenReturn("true");
        assertTrue(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData),
                "HearingIsLinkedFlag should be true");
        when(caseData.getHearingIsLinkedFlag()).thenReturn("false");
        assertFalse(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData),
                "HearingIsLinkedFlag should be false");
    }

    @Test
    void testGetTribunalAndOfficeLocation() {
        assertNull(HearingsDetailsMapping.getTribunalAndOfficeLocation(caseData),
                "TribunalAndOfficeLocation should be null");
    }
}
