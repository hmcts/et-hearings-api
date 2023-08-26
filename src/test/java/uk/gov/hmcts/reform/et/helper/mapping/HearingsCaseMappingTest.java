package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;

class HearingsCaseMappingTest {

    @Mock
    private ReferenceDataServiceHolder holder;

    @BeforeEach
    void setUp() {
        holder = new ReferenceDataServiceHolder();
    }

    @Test
    void testGetHmctsServiceId() {
        holder.setHmctsServiceId("BHA1");

        assertEquals("BHA1", holder.getHmctsServiceId());
    }

    @Test
    void testGetCaseDeepLink() {
        CaseData caseData = new CaseData();
        caseData.setCaseDeepLink("deepLink");

        assertEquals("deepLink", HearingsCaseMapping.getCaseDeepLink(caseData));
    }

    @Test
    void testGetCaseNameHmctsInternal() {
        CaseData caseData = new CaseData();
        caseData.setCaseNameHmctsInternal("nameInternal");

        assertEquals("nameInternal", HearingsCaseMapping.getCaseNameHmctsInternal(caseData));
    }

    @Test
    void testGetPublicCaseName() {
        CaseData caseData = new CaseData();
        caseData.setPublicCaseName("publicName");

        assertEquals("publicName", HearingsCaseMapping.getPublicCaseName(caseData));
    }

    @Test
    void testGetCaseRestrictedFlag() {
        CaseData caseData = new CaseData();
        caseData.setCaseRestrictedFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseRestrictedFlag(caseData));
    }

    @Test
    void testGetCaseAdditionalSecurityFlag() {
        CaseData caseData = new CaseData();
       caseData.setCaseAdditionalSecurityFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData));
    }

    @Test
    void testGetCaseInterpreterRequiredFlag() {
        CaseData caseData = new CaseData();
        caseData.setCaseInterpreterRequiredFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData));
    }

    @Test
    void testGetCaseCreated() {
        CaseData caseData = new CaseData();
        caseData.setReceiptDate("2023-08-26");

        assertEquals("2023-08-26", HearingsCaseMapping.getCaseCreated(caseData));
    }
}

