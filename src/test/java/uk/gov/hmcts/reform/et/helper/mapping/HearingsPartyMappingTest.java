package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HearingsPartyMappingTest {

    @Test
    void testBuildPartyObjectForHearingPayload() throws IOException, URISyntaxException {
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        ServiceHearingValues serviceHearingValues = new CaseTestData().expectedServiceHearingValues();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        assertEquals(serviceHearingValues.getParties(), parties);
    }
}
