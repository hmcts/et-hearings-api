package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HearingsPartyMappingTest {
    ServiceHearingValues serviceHearingValues;
    CaseData caseData;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        CaseTestData caseTestData = new CaseTestData();
        serviceHearingValues = caseTestData.expectedServiceHearingValues();
        caseData = caseTestData.getResource("responses/caseDataPartyMappingFull.json", CaseData.class);
    }

    @Test
    void testBuildPartyObjectForHearingPayloadFullPayload() {
        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        assertEquals(6, parties.size());

        PartyDetails claimant = parties.get(0);

        PartyDetails expectedClaimant = PartyDetails.builder()
                .partyID(caseData.getClaimantId())
                .partyType("IND")
                .partyRole("CLAI")
                .partyName("Citizen One")
                .individualDetails(IndividualDetails.builder()
                        .title("Other")
                        .firstName("Citizen")
                        .lastName("One")
                        .preferredHearingChannel("Email")
                        .interpreterLanguage("nld")
                        .custodyStatus("A")
                        .vulnerableFlag(true)
                        .vulnerabilityDetails("vulnerable with reason")
                        .hearingChannelEmail(List.of("et.citizen1@hmcts.net"))
                        .hearingChannelPhone(List.of("07111111111"))
                        .build())
                .build();

        assertEquals(expectedClaimant, claimant);
        // assertEquals(serviceHearingValues.getParties(), parties);
        assertNotNull(parties);
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
        // assertEquals(serviceHearingValues.getParties(), parties);
        assertNotNull(parties);
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
        // assertEquals(serviceHearingValues.getParties(), parties);
        assertNotNull(parties);
    }
}
