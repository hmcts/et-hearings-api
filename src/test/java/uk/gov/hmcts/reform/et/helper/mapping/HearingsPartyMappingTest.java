package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeR;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HearingsPartyMappingTest {

    public static final String CLAIMANT = "Liz Lemon";
    public static final String CLAIMANT_ID = "123";
    public static final String CLAIMANT_REP = "Liz Lemon's legal rep";
    public static final String CLAIMANT_REP_ID = "456";
    public static final String CLAIMANT_REP_ORG = "Liz Lemon's Legal Firm";
    public static final String RESPONDENT_1 = "Jack Donaghy";
    public static final String RESPONDENT_1_ID = "789";
    public static final String RESPONDENT_1_REP = "Jack Donaghy's legal rep";
    public static final String RESPONDENT_1_REP_ID = "1234";
    public static final String RESPONDENT_1_REP_ORG = "Jack Donaghy's Legal Firm";
    public static final String RESPONDENT_2 = "Tracy Jordan";
    public static final String RESPONDENT_2_ID = "44444";
    public static final String RESPONDENT_2_REP = "Tracy Jordan's legal rep";
    public static final String RESPONDENT_2_REP_ID = "7876";
    private static final String MSG_CLAIMANT_DETAILS_MISMATCH =
        "Claimant party details do not match the expected values";
    private static final String MSG_RESPONDENT_DETAILS_MISMATCH =
        "Respondent party details do not match the expected values";
    private static final String MSG_LEGAL_REP_DETAILS_MISMATCH =
        "Legal representative party details do not match the expected values";

    @Test
    void testBuildPartyObjectForHearingPayload() throws IOException, URISyntaxException {
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapRequestCaseDataToCaseData(mockCaseDetails.getData());
        CaseData claimantRepData = prepareClaimantRepresentative(caseData);

        List<RespondentSumTypeItem> respondentItems = prepareRespondentItems();
        List<RepresentedTypeRItem> respondentRepresentatives = prepareRespondentRepresentatives();

        List<PartyDetails> parties = HearingsPartyMapping.buildPartyObjectForHearingPayload(
            claimantRepData, respondentItems, respondentRepresentatives);

        assertEquals(6, parties.size(), "Expected six parties in total");

        assertClaimantParty(parties.get(0));
        assertClaimantRepParty(parties.get(1));

        assertRespondentParty(parties.get(2), RESPONDENT_1_ID, RESPONDENT_1);
        assertRespondentParty(parties.get(3), RESPONDENT_2_ID, RESPONDENT_2);

        assertLegalRepParty(parties.get(4), RESPONDENT_1_REP_ID, RESPONDENT_1_REP, HearingsPartyMapping.ORGANISATION);
        assertLegalRepParty(parties.get(5), RESPONDENT_2_REP_ID, RESPONDENT_2_REP, HearingsPartyMapping.INDIVIDUAL);
    }

    private void assertPartyDetails(
        PartyDetails party,
        String expectedId,
        String expectedName,
        String expectedRole,
        String expectedType,
        String message) {
        assertEquals(expectedId, party.getPartyID(), message);
        assertEquals(expectedName, party.getPartyName(), message);
        assertEquals(expectedRole, party.getPartyRole(), message);
        assertEquals(expectedType, party.getPartyType(), message);
    }

    private void assertClaimantParty(PartyDetails party) {
        String claimantHmcReference = EntityRoleCode.getHmcReferenceForRole(EntityRoleCode.CLAIMANT);
        assertPartyDetails(
            party,
            CLAIMANT_ID,
            CLAIMANT,
            claimantHmcReference,
            HearingsPartyMapping.INDIVIDUAL,
            MSG_CLAIMANT_DETAILS_MISMATCH
        );
    }

    private void assertClaimantRepParty(PartyDetails party) {
        String legalRepHmcReference = EntityRoleCode.getHmcReferenceForRole(EntityRoleCode.LEGAL_REPRESENTATIVE);
        assertPartyDetails(
            party,
            CLAIMANT_REP_ID,
            CLAIMANT_REP,
            legalRepHmcReference,
            HearingsPartyMapping.ORGANISATION,
            MSG_LEGAL_REP_DETAILS_MISMATCH
        );
    }

    private void assertRespondentParty(PartyDetails party, String expectedId, String expectedName) {
        String respondentHmcReference = EntityRoleCode.getHmcReferenceForRole(EntityRoleCode.RESPONDENT);
        assertPartyDetails(
            party,
            expectedId,
            expectedName,
            respondentHmcReference,
            HearingsPartyMapping.INDIVIDUAL,
            MSG_RESPONDENT_DETAILS_MISMATCH
        );
    }

    private void assertLegalRepParty(
        PartyDetails party,
        String expectedId,
        String expectedName,
        String expectedType) {
        String legalRepHmcReference = EntityRoleCode.getHmcReferenceForRole(EntityRoleCode.LEGAL_REPRESENTATIVE);
        assertPartyDetails(
            party,
            expectedId,
            expectedName,
            legalRepHmcReference,
            expectedType,
            MSG_LEGAL_REP_DETAILS_MISMATCH
        );
    }

    private List<RespondentSumTypeItem> prepareRespondentItems() {
        final List<RespondentSumTypeItem> respondentItems = new ArrayList<>();

        RespondentSumTypeItem respondent1 = new RespondentSumTypeItem();
        respondent1.setId(RESPONDENT_1_ID);
        RespondentSumType respondentSumType1 = new RespondentSumType();
        respondentSumType1.setRespondentName(RESPONDENT_1);
        respondent1.setValue(respondentSumType1);
        respondentItems.add(respondent1);

        RespondentSumTypeItem respondent2 = new RespondentSumTypeItem();
        respondent2.setId(RESPONDENT_2_ID);
        RespondentSumType respondentSumType2 = new RespondentSumType();
        respondentSumType2.setRespondentName(RESPONDENT_2);
        respondent2.setValue(respondentSumType2);
        respondentItems.add(respondent2);

        return respondentItems;
    }

    private CaseData prepareClaimantRepresentative(CaseData caseData) {
        caseData.setClaimantId(CLAIMANT_ID);
        caseData.setClaimant(CLAIMANT);
        caseData.getRepresentativeClaimantType().setRepresentativeId(CLAIMANT_REP_ID);
        caseData.getRepresentativeClaimantType().setNameOfRepresentative(CLAIMANT_REP);
        caseData.getRepresentativeClaimantType().setNameOfOrganisation(CLAIMANT_REP_ORG);

        return caseData;
    }

    private List<RepresentedTypeRItem> prepareRespondentRepresentatives() {
        final List<RepresentedTypeRItem> respondentRepresentatives = new ArrayList<>();
        RepresentedTypeRItem respondentRep1 = new RepresentedTypeRItem();
        respondentRep1.setId(RESPONDENT_1_REP_ID);
        RepresentedTypeR representedTypeR = new RepresentedTypeR();
        representedTypeR.setNameOfRepresentative(RESPONDENT_1_REP);
        representedTypeR.setNameOfOrganisation(RESPONDENT_1_REP_ORG);
        respondentRep1.setValue(representedTypeR);

        RepresentedTypeRItem respondentRep2 = new RepresentedTypeRItem();
        respondentRep2.setId(RESPONDENT_2_REP_ID);
        RepresentedTypeR representedTypeR2 = new RepresentedTypeR();
        representedTypeR2.setNameOfRepresentative(RESPONDENT_2_REP);
        respondentRep2.setValue(representedTypeR2);
        respondentRepresentatives.add(respondentRep1);
        respondentRepresentatives.add(respondentRep2);

        return respondentRepresentatives;
    }
}
