package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.CaseCategory;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;
import static uk.gov.hmcts.reform.et.helper.mapping.HearingsCaseMapping.CASE_TYPE;
import static uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder.DEFAULT_CATEGORY;

class HearingsCaseMappingTest {

    @Test
    void testGetHmctsServiceId() {
        ReferenceDataServiceHolder referenceDataServiceHolder = new ReferenceDataServiceHolder();
        referenceDataServiceHolder.setHmctsServiceId("ServiceId");
        String hmctsServiceId = referenceDataServiceHolder.getHmctsServiceId();

        assertEquals("ServiceId", hmctsServiceId, "HmctsServiceId should match the expected value");
    }

    @Test
    void testGetCaseDeepLink() {
        CaseData caseData = new CaseData();
        caseData.setCaseDeepLink("deepLink");

        assertEquals("deepLink", HearingsCaseMapping.getCaseDeepLink(caseData),
                "CaseDeepLink should match the expected value");
    }

    @Test
    void testGetCaseNameHmctsInternal() {
        CaseData caseData = new CaseData();
        caseData.setCaseNameHmctsInternal("nameInternal");

        assertEquals("nameInternal", HearingsCaseMapping.getCaseNameHmctsInternal(caseData),
                "CaseNameHmctsInternal should match the expected value");
    }

    @Test
    void testGetPublicCaseName() {
        CaseData caseData = new CaseData();
        caseData.setPublicCaseName("publicName");

        assertEquals("publicName", HearingsCaseMapping.getPublicCaseName(caseData),
                "PublicCaseName should match the expected value");
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
                "CaseCreated should match the expected value");
    }

    @Test
    void testGetCaseCategories() {
        List<CaseCategory> result = HearingsCaseMapping.getCaseCategories();
        assertEquals(1, result.size(), "There should be one case category");

        CaseCategory caseCategory = result.get(0);
        assertEquals(CASE_TYPE, caseCategory.getCategoryType(),
                "CategoryType should match the expected value");
        assertEquals(DEFAULT_CATEGORY, caseCategory.getCategoryValue(),
                "CategoryValue should match the expected value");
        assertEquals(DEFAULT_CATEGORY, caseCategory.getCategoryParent(),
                "CategoryParent should match the expected value");
    }
}
