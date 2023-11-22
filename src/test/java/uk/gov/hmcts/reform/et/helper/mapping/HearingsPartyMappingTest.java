package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.OrganisationDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HearingsPartyMappingTest {
    ServiceHearingValues serviceHearingValues;
    CaseData caseData;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        CaseTestData caseTestData = new CaseTestData();
        String serviceHearingValuesFile = "partyMapping/serviceHearingValues.json";
        serviceHearingValues = caseTestData.getResource(serviceHearingValuesFile, ServiceHearingValues.class);
        caseData = caseTestData.getResource("partyMapping/caseData.json", CaseData.class);
    }

    @Test
    void testBuildPartyObjectForHearingPayloadFullPayload() {
        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        List<PartyDetails> expectedParties = serviceHearingValues.getParties();

        assertEquals(8, actualParties.size());

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoClaimantRep() {
        List<PartyDetails> expectedParties = serviceHearingValues.getParties();
        caseData.setRepresentativeClaimantType(null);
        expectedParties.remove(2); // Removing claimant rep's firm
        expectedParties.remove(1); // Removing claimant rep

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        assertEquals(6, actualParties.size());

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadRespondentCompany() {
        List<PartyDetails> expectedParties = serviceHearingValues.getParties();
        caseData.getRespondentCollection().get(0).getValue().setRespondentOrganisation("Dream Corp LLC");
        PartyDetails expectedRespondent = expectedParties.get(3);
        expectedRespondent.setIndividualDetails(null);
        expectedRespondent.setPartyType("ORG");
        expectedRespondent.setPartyName("Dream Corp LLC");
        expectedRespondent.setOrganisationDetails(OrganisationDetails.builder()
                .organisationType("ORG")
                .name("Dream Corp LLC")
                .build());

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        assertEquals(8, actualParties.size());

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoFirstLastName() {
        RespondentSumType respondent = caseData.getRespondentCollection().get(0).getValue();
        respondent.setRespondentFirstName(null);
        respondent.setRespondentLastName(null);
        respondent.setRespondentName("First Last");

        List<PartyDetails> expectedParties = serviceHearingValues.getParties();
        PartyDetails expectedRespondentDetails = expectedParties.get(3);
        expectedRespondentDetails.setPartyName("First Last");
        expectedRespondentDetails.getIndividualDetails().setFirstName("First");
        expectedRespondentDetails.getIndividualDetails().setLastName("Last");

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        assertEquals(expectedParties, actualParties);

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoLastName() {
        RespondentSumType respondent = caseData.getRespondentCollection().get(0).getValue();
        respondent.setRespondentFirstName(null);
        respondent.setRespondentLastName(null);
        String noLastName = "NoLastName";
        respondent.setRespondentName(noLastName);

        List<PartyDetails> expectedParties = serviceHearingValues.getParties();
        PartyDetails expectedRespondentDetails = expectedParties.get(3);
        expectedRespondentDetails.setPartyName(noLastName);
        expectedRespondentDetails.getIndividualDetails().setFirstName(noLastName);
        expectedRespondentDetails.getIndividualDetails().setLastName(noLastName);

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoFlags() {
        List<PartyDetails> expectedParties = serviceHearingValues.getParties();

        caseData.getClaimantFlags().setDetails(null);
        caseData.getRespondentFlags().setDetails(null);

        IndividualDetails claimantIndividualDetails = expectedParties.get(0).getIndividualDetails();
        claimantIndividualDetails.setVulnerableFlag(false);
        claimantIndividualDetails.setVulnerabilityDetails(null);
        claimantIndividualDetails.setInterpreterLanguage(null);

        IndividualDetails respondentIndividualDetails = expectedParties.get(3).getIndividualDetails();
        respondentIndividualDetails.setVulnerableFlag(false);
        respondentIndividualDetails.setVulnerabilityDetails(null);
        respondentIndividualDetails.setInterpreterLanguage(null);

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);
        assertEquals(8, actualParties.size());

        for (int i = 0; i < actualParties.size(); i++) {
            assertEquals(expectedParties.get(i), actualParties.get(i));
        }
    }

    @Test
    void testBuildPartyObjectForHearingPayloadNoOrgNames() {
        String claimantRepName = "claimantRepName";
        String resRepName = "resRepName";

        List<PartyDetails> expectedParties = serviceHearingValues.getParties();
        caseData.getRepresentativeClaimantType().setNameOfOrganisation(null);
        caseData.getRepresentativeClaimantType().setNameOfRepresentative(claimantRepName);
        caseData.getRepCollection().get(0).getValue().setNameOfOrganisation(null);
        caseData.getRepCollection().get(0).getValue().setNameOfRepresentative(resRepName);


        PartyDetails expectedClaimant = expectedParties.get(2);
        expectedClaimant.getOrganisationDetails().setName(claimantRepName);
        expectedClaimant.setPartyName(claimantRepName);


        PartyDetails expectedRespondent = expectedParties.get(5);
        expectedRespondent.getOrganisationDetails().setName(resRepName);
        expectedRespondent.setPartyName(resRepName);

        List<PartyDetails> actualParties = HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData);

        PartyDetails actualClaimant = actualParties.get(2);
        assertEquals(expectedClaimant.getPartyName(), actualClaimant.getPartyName());
        assertEquals(expectedClaimant.getOrganisationDetails().getName(),
                actualClaimant.getOrganisationDetails().getName());

        PartyDetails actualRespondent = actualParties.get(5);
        assertEquals(expectedRespondent.getPartyName(), actualRespondent.getPartyName());
        assertEquals(expectedRespondent.getOrganisationDetails().getName(),
                actualRespondent.getOrganisationDetails().getName());

    }
}
