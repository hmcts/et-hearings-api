package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaseFlagsMappingTest {
    @Test
    void testGetCaseFlags() throws IOException, URISyntaxException {
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());
        ServiceHearingValues serviceHearingValues = new CaseTestData().expectedServiceHearingValues();

        CaseFlags actual = CaseFlagsMapping.getCaseFlags(caseData);
        assertEquals(serviceHearingValues.getCaseFlags(), actual);
    }
}
