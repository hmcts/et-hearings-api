package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.CLAIMANT;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.LEGAL_REPRESENTATIVE;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.RESPONDENT;

public final class HearingsPartyMapping {

    public static final String INDIVIDUAL = "IND";
    public static final String ORGANISATION = "ORG";

    private HearingsPartyMapping() {
        //NO-OP
    }

    public static List<PartyDetails> buildPartyObjectForHearingPayload(
        CaseData caseData,
        List<RespondentSumTypeItem> respondents,
        List<RepresentedTypeRItem> representatives) {

        List<PartyDetails> parties = new ArrayList<>();
        addPartyObjects(caseData, parties, representatives, respondents);
        return parties;
    }

    private static void addPartyObjects(CaseData caseData,
                                        List<PartyDetails> parties,
                                        List<RepresentedTypeRItem> representative,
                                        List<RespondentSumTypeItem> respondents) {

        parties.add(getDetailsForClaimantPartyObject(caseData));

        if (caseData.getRepresentativeClaimantType() != null
            && caseData.getRepresentativeClaimantType().getNameOfRepresentative() != null
            && !caseData.getRepresentativeClaimantType().getNameOfRepresentative().isEmpty()) {
            parties.add(getDetailsForClaimantRepPartyObject(caseData));
        }

        parties.addAll(getDetailsForRespondentPartyObject(respondents));

        if (representative != null && !representative.isEmpty()) {
            parties.addAll(getDetailsForRespondentRepPartyObject(representative));
        }
    }

    private static List<PartyDetails> getDetailsForRespondentPartyObject(List<RespondentSumTypeItem> respondents) {
        return respondents.stream()
            .map(respondentItem -> {
                PartyDetails respondentDetails = new PartyDetails();
                respondentDetails.setPartyID(respondentItem.getId());
                respondentDetails.setPartyRole(RESPONDENT.getHmcReference());

                if (respondentItem.getValue().getRespondentOrganisation() == null
                    || respondentItem.getValue().getRespondentOrganisation().isEmpty()) {
                    respondentDetails.setPartyType(INDIVIDUAL);
                    respondentDetails.setPartyName(respondentItem.getValue().getRespondentName());
                } else {
                    respondentDetails.setPartyType(ORGANISATION);
                    respondentDetails.setPartyName(respondentItem.getValue().getRespondentOrganisation());
                }
                return respondentDetails;
            })
            .toList();
    }

    private static List<PartyDetails> getDetailsForRespondentRepPartyObject(
        List<RepresentedTypeRItem> representatives) {
        return representatives.stream()
            .map(repItem -> {
                PartyDetails respondentRepDetails = new PartyDetails();
                respondentRepDetails.setPartyID(repItem.getId());
                respondentRepDetails.setPartyName(repItem.getValue().getNameOfRepresentative());
                respondentRepDetails.setPartyRole(LEGAL_REPRESENTATIVE.getHmcReference());

                if (repItem.getValue().getNameOfOrganisation() == null
                    || repItem.getValue().getNameOfOrganisation().isEmpty()) {
                    respondentRepDetails.setPartyType(INDIVIDUAL);
                } else {
                    respondentRepDetails.setPartyType(ORGANISATION);
                }
                return respondentRepDetails;
            })
            .toList();
    }

    private static PartyDetails getDetailsForClaimantPartyObject(CaseData caseData) {
        PartyDetails claimantDetails = new PartyDetails();

        claimantDetails.setPartyID(caseData.getClaimantId());
        claimantDetails.setPartyName(caseData.getClaimant());
        claimantDetails.setPartyRole(CLAIMANT.getHmcReference());
        claimantDetails.setPartyType(INDIVIDUAL);

        return claimantDetails;
    }

    private static PartyDetails getDetailsForClaimantRepPartyObject(CaseData caseData) {
        PartyDetails claimantRepDetails = new PartyDetails();
        claimantRepDetails.setPartyID(caseData.getRepresentativeClaimantType().getRepresentativeId());
        claimantRepDetails.setPartyName(caseData.getRepresentativeClaimantType().getNameOfRepresentative());
        claimantRepDetails.setPartyRole(LEGAL_REPRESENTATIVE.getHmcReference());

        if (caseData.getRepresentativeClaimantType().getNameOfOrganisation() == null
            || caseData.getRepresentativeClaimantType().getNameOfOrganisation().isEmpty()) {
            claimantRepDetails.setPartyType(INDIVIDUAL);
        } else {
            claimantRepDetails.setPartyType(ORGANISATION);
        }
        return claimantRepDetails;
    }
}

