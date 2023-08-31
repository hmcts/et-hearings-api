package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.HearingType;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ServiceHearingValuesMappingTest {

    public static final String PUBLIC_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";
    public static final String HMCTS_INTERNAL_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";

    @Mock
    private CaseDetails caseDetails;

    @Mock
    private CaseData caseData;

    @Mock
    private ReferenceDataServiceHolder referenceDataServiceHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapServiceHearingValues() {
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();
        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId("test_ID");

        final HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("2");
        hearingValue.setHearingEstLengthNumType("Days");
        hearingValue.setHearingType("Hearing");
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        when(caseDetails.getId()).thenReturn(Long.valueOf("123"));
        when(caseData.getAutoListFlag()).thenReturn(String.valueOf(false));
        when(caseData.getPublicCaseName()).thenReturn(PUBLIC_CASE_NAME);
        when(caseData.getCaseDeepLink()).thenReturn("/documents/deep/link");
        when(caseData.getCaseNameHmctsInternal()).thenReturn(HMCTS_INTERNAL_CASE_NAME);
        when(caseData.getReceiptDate()).thenReturn(null);
        when(caseData.getHearingType()).thenReturn("Hearing");
        when(caseData.getDuration()).thenReturn(720);
        when(caseData.getCaseRestrictedFlag()).thenReturn(String.valueOf(false));
        when(caseData.getHearingPriorityType()).thenReturn("Standard");
        when(caseData.getNumberOfPhysicalAttendees()).thenReturn(0);
        when(caseData.getHearingInWelshFlag()).thenReturn(String.valueOf(false));
        when(caseData.getCaseAdditionalSecurityFlag()).thenReturn(String.valueOf(false));
        when(caseData.getPrivateHearingRequiredFlag()).thenReturn(String.valueOf(false));
        when(caseData.getLeadJudgeContractType()).thenReturn("");
        when(caseData.getHearingIsLinkedFlag()).thenReturn(String.valueOf(false));
        when(caseData.getHmctsServiceID()).thenReturn("BHA1");
        when(referenceDataServiceHolder.getHmctsServiceId()).thenReturn("ServiceId");

        ServiceHearingValues serviceHearingValues = ServiceHearingValuesMapping.mapServiceHearingValues(
            caseDetails,
            caseData,
            "test_ID",
            hearingCollection,
            referenceDataServiceHolder
        );

        assertEquals("ServiceId", serviceHearingValues.getHmctsServiceId(),
                     "HmctsServiceId should match expected value"
        );
        assertFalse(serviceHearingValues.isAutoListFlag(), "is auto list flag");
        assertEquals(PUBLIC_CASE_NAME, serviceHearingValues.getPublicCaseName(),
                     "get public case name"
        );
        assertEquals("/documents/deep/link", serviceHearingValues.getCaseDeepLink(),
                     "get case deep link"
        );
        assertEquals(HMCTS_INTERNAL_CASE_NAME, serviceHearingValues.getCaseNameHmctsInternal(),
                     "hmcts internal case name"
        );
        assertNull(serviceHearingValues.getReceiptDate(), "case SLA start date");
        assertFalse(serviceHearingValues.isCaseRestrictedFlag(), "case restricted flag");
        assertEquals("Hearing", serviceHearingValues.getHearingType(), "hearing type");
        assertEquals(720, serviceHearingValues.getDuration(), "duration");
        assertEquals("Standard", serviceHearingValues.getHearingPriorityType(),
                     "hearing priority type"
        );
        assertEquals(0, serviceHearingValues.getNumberOfPhysicalAttendees(),
                     "no. of physical attendees"
        );
        assertFalse(serviceHearingValues.isHearingInWelshFlag(), "hearing in Welsh flag");
        assertFalse(serviceHearingValues.getCaseAdditionalSecurityFlag(), "additional security flag");
        assertFalse(serviceHearingValues.isPrivateHearingRequiredFlag(), "private hearing required");
        assertEquals("", serviceHearingValues.getLeadJudgeContractType(), "lead judge contract");
        assertFalse(serviceHearingValues.isHearingIsLinkedFlag(), "is hearing linked");
    }
}
