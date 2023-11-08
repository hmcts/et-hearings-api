package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HearingsPartyMappingTest {
    CaseDetails mockCaseDetails;
    ServiceHearingValues serviceHearingValues;
    CaseData caseData;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        mockCaseDetails = new CaseTestData().expectedDetails();
        serviceHearingValues = new CaseTestData().expectedServiceHearingValues();
        caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());
    }

    @Test
    void testBuildPartyObjectForHearingPayloadFullPayload() {
        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        assertEquals(serviceHearingValues.getParties(), parties);
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoFirstLastName() {
        RespondentSumType respondent = caseData.getRespondentCollection().get(0).getValue();
        respondent.setRespondentFirstName(null);
        respondent.setRespondentLastName(null);
        respondent.setRespondentName("First Last");

        PartyDetails expectedRespondentDetails = serviceHearingValues.getParties().get(2);
        expectedRespondentDetails.setPartyName("First Last");
        expectedRespondentDetails.getIndividualDetails().setFirstName("First");
        expectedRespondentDetails.getIndividualDetails().setLastName("Last");

        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        assertEquals(serviceHearingValues.getParties(), parties);
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoLastName() {
        RespondentSumType respondent = caseData.getRespondentCollection().get(0).getValue();
        respondent.setRespondentFirstName(null);
        respondent.setRespondentLastName(null);
        String noLastName = "NoLastName";
        respondent.setRespondentName(noLastName);

        PartyDetails expectedRespondentDetails = serviceHearingValues.getParties().get(2);
        expectedRespondentDetails.setPartyName(noLastName);
        expectedRespondentDetails.getIndividualDetails().setFirstName(noLastName);
        expectedRespondentDetails.getIndividualDetails().setLastName(noLastName);

        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        assertEquals(serviceHearingValues.getParties(), parties);
    }
}
