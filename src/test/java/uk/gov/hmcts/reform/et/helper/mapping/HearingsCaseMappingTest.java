package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;

class HearingsCaseMappingTest {

    @Test
    void testGetHmctsServiceId() {
        ReferenceDataServiceHolder referenceDataServiceHolder = new ReferenceDataServiceHolder();
        referenceDataServiceHolder.setHmctsServiceId("ServiceId");
        String hmctsServiceId = referenceDataServiceHolder.getHmctsServiceId();

        assertEquals("ServiceId", hmctsServiceId, "HmctsServiceId should match expected value");
    }

    @Test
    void testGetCaseDeepLink() {
        CaseData caseData = new CaseData();
        caseData.setCaseDeepLink("deepLink");

        assertEquals("deepLink", HearingsCaseMapping.getCaseDeepLink(caseData),
                     "CaseDeepLink should match expected value");
    }

    @Test
    void testGetCaseNameHmctsInternal() {
        CaseData caseData = new CaseData();
        caseData.setCaseNameHmctsInternal("nameInternal");

        assertEquals("nameInternal", HearingsCaseMapping.getCaseNameHmctsInternal(caseData),
                     "CaseNameHmctsInternal should match expected value");
    }

    @Test
    void testGetPublicCaseName() {
        CaseData caseData = new CaseData();
        caseData.setPublicCaseName("publicName");

        assertEquals("publicName", HearingsCaseMapping.getPublicCaseName(caseData),
                     "PublicCaseName should match expected value");
    }

    @Test
    void testGetCaseRestrictedFlag() {
        CaseData caseData = new CaseData();
        caseData.setCaseRestrictedFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseRestrictedFlag(caseData), "CaseRestrictedFlag should be true");
    }

    @Test
    void testGetCaseAdditionalSecurityFlag() {
        CaseData caseData = new CaseData();
        caseData.setCaseAdditionalSecurityFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData),
                   "CaseAdditionalSecurityFlag should be true");
    }

    @Test
    void testGetCaseInterpreterRequiredFlag() {
        CaseData caseData = new CaseData();
        caseData.setCaseInterpreterRequiredFlag(YES);

        assertTrue(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData),
                   "CaseInterpreterRequiredFlag should be true");
    }

    @Test
    void testGetCaseCreated() {
        CaseData caseData = new CaseData();
        caseData.setReceiptDate("2023-08-26");

        assertEquals("2023-08-26", HearingsCaseMapping.getCaseCreated(caseData),
                     "CaseCreated should match expected value");
    }
}
