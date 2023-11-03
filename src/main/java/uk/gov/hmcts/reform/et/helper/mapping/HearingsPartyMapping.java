package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeC;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public final class HearingsPartyMapping {

    public static final String INDIVIDUAL = "IND";
    public static final String ORGANISATION = "ORG";

    private HearingsPartyMapping() {

    }

    public static List<PartyDetails> buildPartyObjectForHearingPayload(CaseData caseData) {
        List<PartyDetails> parties = new ArrayList<>();
        parties.add(mapPartyDetailsForClaimant(caseData));

        Optional.ofNullable(caseData.getRepresentativeClaimantType())
                .filter(claimant -> isNotEmpty(claimant.getNameOfRepresentative()))
                .ifPresent(claimant -> parties.add(mapPartyDetailsForClaimantRep(caseData)));

        Optional.ofNullable(caseData.getRespondentCollection())
                .ifPresent(respondents -> parties.addAll(respondents.stream()
                    .map(HearingsPartyMapping::mapPartyDetailsForRespondent)
                    .toList()));

        Optional.ofNullable(caseData.getRepCollection())
                .ifPresent(representatives -> parties.addAll(representatives.stream()
                        .map(HearingsPartyMapping::mapPartyDetailsForRespondentRep)
                        .toList()));

        return parties;
    }

    private static PartyDetails mapPartyDetailsForRespondent(RespondentSumTypeItem respondentItem) {
        RespondentSumType respondent = respondentItem.getValue();
        assert respondent != null;

        PartyDetails details = PartyDetails.builder()
                .partyID(respondentItem.getId())
                .partyRole(EntityRoleCode.RESPONDENT.getHmcReference())
                .partyType(ORGANISATION)
                .partyName(respondent.getRespondentOrganisation())
                .build();

        if (isNotEmpty(details.getPartyName())) {
            return details; // Return early as we've already defaulted to organisation
        }

        String fullName = respondent.getRespondentName();
        details.setPartyType(INDIVIDUAL);
        details.setPartyName(fullName);

        String firstName = defaultString(respondent.getRespondentFirstName(), fullName);
        String lastName = defaultString(respondent.getRespondentLastName(), fullName);

        if (isEmpty(respondent.getRespondentFirstName()) && fullName.contains(" ")) {
            // Need to and can split by space
            int lastSpaceIndex = fullName.lastIndexOf(' ');
            firstName = fullName.substring(0, lastSpaceIndex);
            lastName = fullName.substring(lastSpaceIndex + 1);
        }

        details.setIndividualDetails(IndividualDetails.builder().firstName(firstName).lastName(lastName).build());
        return details;
    }

    private static PartyDetails mapPartyDetailsForRespondentRep(RepresentedTypeRItem repItem) {
        // TODO: Do we need IndividualDetails for RespondentRep?
        return PartyDetails.builder()
                .partyID(repItem.getId())
                .partyName(repItem.getValue().getNameOfRepresentative())
                .partyRole(EntityRoleCode.LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(isEmpty(repItem.getValue().getNameOfOrganisation()) ? INDIVIDUAL : ORGANISATION)
                .build();
    }

    private static PartyDetails mapPartyDetailsForClaimant(CaseData caseData) {
        IndividualDetails individualDetails = IndividualDetails.builder()
                .title(caseData.getClaimantIndType().getClaimantTitle())
                .firstName(caseData.getClaimantIndType().getClaimantFirstNames())
                .lastName(caseData.getClaimantIndType().getClaimantLastName())
                .build();

        return PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(caseData.getClaimantId()))
                .partyID(defaultString(caseData.getClaimantId(), UUID.randomUUID().toString()))
                .partyName(caseData.getClaimant())
                .partyRole(EntityRoleCode.CLAIMANT.getHmcReference())
                .partyType(INDIVIDUAL)
                .individualDetails(individualDetails).build();
    }

    private static PartyDetails mapPartyDetailsForClaimantRep(CaseData caseData) {
        RepresentedTypeC claimantType = caseData.getRepresentativeClaimantType();

        // TODO: Do we need IndividualDetails for ClaimantRep?
        return PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(claimantType.getRepresentativeId())
                .partyID(defaultString(claimantType.getRepresentativeId(), UUID.randomUUID().toString()))
                .partyName(claimantType.getNameOfRepresentative())
                .partyRole(EntityRoleCode.LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(isEmpty(claimantType.getNameOfOrganisation()) ? INDIVIDUAL : ORGANISATION)
                .build();
    }
}

