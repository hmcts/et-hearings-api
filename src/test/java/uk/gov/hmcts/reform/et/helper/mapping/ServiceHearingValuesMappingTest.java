package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.bulk.types.DynamicFixedListType;
import uk.gov.hmcts.et.common.model.bulk.types.DynamicValueType;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLocation;
import uk.gov.hmcts.et.common.model.ccd.types.ClaimantIndType;
import uk.gov.hmcts.et.common.model.ccd.types.HearingType;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeR;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.CaseCategory;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.et.helper.mapping.HearingVenueTest.BRISTOL;
import static uk.gov.hmcts.reform.et.helper.mapping.HearingVenueTest.MOCK_REQUEST_HEARING_ID;
import static uk.gov.hmcts.reform.et.helper.mapping.HearingsPartyMappingTest.RESPONDENT_1_REP_ORG;
import static uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder.DEFAULT_CATEGORY;

class ServiceHearingValuesMappingTest {

    public static final String PUBLIC_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";
    public static final String HMCTS_INTERNAL_CASE_NAME = "Johnny Claimant v Acme Redde Ltd";
    public static final String CASE_ID = "1646225213651590";

    @Mock
    private CaseDetails caseDetails;

    @Mock
    private CaseData caseData;

    @Mock
    private ReferenceDataServiceHolder referenceDataServiceHolder;
    private List<HearingTypeItem> hearingCollection;
    private List<RespondentSumTypeItem> respondentCollection;
    private List<RepresentedTypeRItem> respondentRepCollection;
    private List<CaseCategory> caseCategoryList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hearingCollection = new ArrayList<>();
        respondentCollection = new ArrayList<>();
        respondentRepCollection = new ArrayList<>();
        caseCategoryList = new ArrayList<>();
    }

    @Test
    void shouldMapServiceHearingValues() {
        setupTestData();

        ServiceHearingValues serviceHearingValues = ServiceHearingValuesMapping.mapServiceHearingValues(
            caseDetails,
            caseData,
            respondentCollection,
            respondentRepCollection
        );

        assertServiceHearingValues(serviceHearingValues);
    }

    private void setupTestData() {
        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(MOCK_REQUEST_HEARING_ID);

        final HearingType hearingValue = new HearingType();
        final List<HearingLocation> hearingLocations = new ArrayList<>();
        final HearingLocation hearingLocation = new HearingLocation();
        DynamicValueType dynamicValueType = new DynamicValueType();
        dynamicValueType.setCode(BRISTOL);
        hearingLocation.setLocationId(BRISTOL);
        hearingValue.setHearingVenue(DynamicFixedListType.of(dynamicValueType));
        hearingValue.setHearingEstLengthNum("2");
        hearingValue.setHearingEstLengthNumType("Days");
        hearingValue.setHearingType("Hearing");
        hearingItem.setValue(hearingValue);

        hearingCollection.add(hearingItem);

        CaseCategory caseCategory = new CaseCategory();
        caseCategory.setCategoryType(DEFAULT_CATEGORY);
        caseCategory.setCategoryValue(DEFAULT_CATEGORY);
        caseCategory.setCategoryParent(DEFAULT_CATEGORY);

        caseCategoryList.add(caseCategory);

        final List<RespondentSumTypeItem> respondentCollection = new ArrayList<>();
        final RespondentSumTypeItem respondentItem = new RespondentSumTypeItem();
        respondentItem.setId("123456");

        final RespondentSumType respondent = new RespondentSumType();
        respondent.setRespondentOrganisation(RESPONDENT_1_REP_ORG);
        respondentItem.setValue(respondent);
        respondentCollection.add(respondentItem);

        final List<RepresentedTypeRItem> respondentRepCollection = new ArrayList<>();
        final RepresentedTypeRItem respondentRepItem = new RepresentedTypeRItem();
        respondentRepItem.setId("1234321");

        final RepresentedTypeR respondentRep = new RepresentedTypeR();
        respondentRep.setNameOfOrganisation(RESPONDENT_1_REP_ORG);
        respondentRepItem.setValue(respondentRep);
        respondentRepCollection.add(respondentRepItem);

        when(caseDetails.getId()).thenReturn(Long.valueOf(CASE_ID));
        when(caseData.getAutoListFlag()).thenReturn(String.valueOf(false));
        when(caseData.getPublicCaseName()).thenReturn(PUBLIC_CASE_NAME);
        when(caseData.getCaseDeepLink()).thenReturn("/documents/deep/link");
        when(caseData.getCaseNameHmctsInternal()).thenReturn(HMCTS_INTERNAL_CASE_NAME);
        when(caseData.getReceiptDate()).thenReturn("2023-02-02");
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
        when(caseData.getHmctsServiceID()).thenReturn("BBA3");
        when(referenceDataServiceHolder.getHmctsServiceId()).thenReturn("BBA3");
        when(caseData.getCaseCategories()).thenReturn(caseCategoryList);
        when(caseData.getRespondentCollection()).thenReturn(respondentCollection);
        when(caseData.getRepCollection()).thenReturn(respondentRepCollection);
        when(caseData.getHearingLocations()).thenReturn(hearingLocations);
        when(caseData.getCaseManagementLocation()).thenReturn(CaseLocation.builder().baseLocation("36313").build());

        ClaimantIndType claimantIndType = new ClaimantIndType();
        claimantIndType.setClaimantFirstNames("First");
        claimantIndType.setClaimantLastName("Last");
        claimantIndType.setClaimantTitle("Mr");

        when(caseData.getClaimantIndType()).thenReturn(claimantIndType);
    }

    private void assertServiceHearingValues(ServiceHearingValues serviceHearingValues) {
        assertEquals("BBA3", serviceHearingValues.getHmctsServiceID(),
                     "HmctsServiceId should match expected value");
        assertFalse(serviceHearingValues.isAutoListFlag(), "Auto list flag should be false");
        assertEquals(PUBLIC_CASE_NAME, serviceHearingValues.getPublicCaseName(),
                     "Public case name should match");
        assertEquals("/documents/deep/link", serviceHearingValues.getCaseDeepLink(),
                     "Case deep link should match");
        assertEquals(HMCTS_INTERNAL_CASE_NAME, serviceHearingValues.getHmctsInternalCaseName(),
                     "Hmcts internal case name should match");
        assertNotNull(serviceHearingValues.getCaseSlaStartDate(), "Receipt date should be null");
        assertFalse(serviceHearingValues.isCaseRestrictedFlag(), "Case restricted flag should be false");
        assertEquals(null, serviceHearingValues.getHearingType(), "Hearing type should match");
        assertEquals(0, serviceHearingValues.getDuration(), "Duration should match");
        assertEquals("Standard", serviceHearingValues.getHearingPriorityType(),
                     "Hearing priority type should match");
        assertEquals(0, serviceHearingValues.getNumberOfPhysicalAttendees(),
                     "Number of physical attendees should match");
        assertFalse(serviceHearingValues.isHearingInWelshFlag(), "Hearing in Welsh flag should be false");
        assertFalse(serviceHearingValues.getCaseAdditionalSecurityFlag(),
                    "Case additional security flag should be false");
        assertFalse(serviceHearingValues.isPrivateHearingRequiredFlag(),
                    "Private hearing required flag should be false");
        assertEquals("", serviceHearingValues.getLeadJudgeContractType(),
                     "Lead judge contract type should match");
        assertFalse(serviceHearingValues.isHearingIsLinkedFlag(), "Hearing is linked flag should be false");
        assertTrue(caseCategoryList.contains(new CaseCategory(DEFAULT_CATEGORY, DEFAULT_CATEGORY, DEFAULT_CATEGORY)),
                   "Expected case category 'Employment' not found");
    }
}
