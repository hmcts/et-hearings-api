package uk.gov.hmcts.reform.et.helper.mapping;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.ClaimantHearingPreference;
import uk.gov.hmcts.et.common.model.ccd.types.Organisation;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeC;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeR;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.OrganisationDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.CLAIMANT;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.LEGAL_REPRESENTATIVE;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.RESPONDENT;

public final class HearingsPartyMapping {

    public static final String INDIVIDUAL = "IND";
    public static final String ORGANISATION = "ORG";

    private HearingsPartyMapping() {

    }

    /**
     * Construct a list of PartyDetails for HMC. Maps Claimant, Respondent and their legal reps where appropriate.
     */
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
                .partyRole(RESPONDENT.getHmcReference())
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
        RepresentedTypeR rep = repItem.getValue();
        PartyDetails partyDetails = PartyDetails.builder()
                .partyID(repItem.getId())
                .partyName(rep.getNameOfRepresentative())
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(isEmpty(rep.getNameOfOrganisation()) ? INDIVIDUAL : ORGANISATION)
                .build();

        if (StringUtils.isEmpty(rep.getNameOfOrganisation())) {
            partyDetails.setIndividualDetails();
        } else {
            Organisation organisation = rep.getRespondentOrganisation();
            partyDetails.setOrganisationDetails(OrganisationDetails.builder()
                    .name(rep.getNameOfOrganisation())
                    .cftOrganisationID(ObjectUtils.isEmpty(organisation) ? null : organisation.getOrganisationID())
                    .build());
        }
        return partyDetails;
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
                .partyRole(CLAIMANT.getHmcReference())
                .partyType(INDIVIDUAL)
                .individualDetails(individualDetails).build();
    }

    private static PartyDetails mapPartyDetailsForClaimantRep(CaseData caseData) {
        RepresentedTypeC claimantType = caseData.getRepresentativeClaimantType();

        // TODO: Do we need IndividualDetails for ClaimantRep?
        PartyDetails partyDetails = PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(claimantType.getRepresentativeId())
                .partyID(defaultString(claimantType.getRepresentativeId(), UUID.randomUUID().toString()))
                .partyName(claimantType.getNameOfRepresentative())
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(isEmpty(claimantType.getNameOfOrganisation()) ? INDIVIDUAL : ORGANISATION)
                .build();

        if (StringUtils.isEmpty(claimantType.getNameOfOrganisation())) {
            partyDetails.setIndividualDetails(mapIndividualDetailsForClaimantRep(claimantType));
        } else {
            partyDetails.setOrganisationDetails(OrganisationDetails.builder()
                    .name(claimantType.getNameOfOrganisation())
                    .build());
        }

        return partyDetails;
    }

    private static IndividualDetails mapIndividualDetailsForClaimantRep(RepresentedTypeC claimantType) {
        return IndividualDetails.builder()
                .firstName(getFirstName(claimantType.getNameOfRepresentative()))
                .lastName(getLastName(claimantType.getNameOfRepresentative()))
                .preferredHearingChannel(claimantType.getRepresentativePreference())
                .hearingChannelEmail(List.of(claimantType.getRepresentativeEmailAddress()))
                .hearingChannelPhone(
                        Stream.of(claimantType.getRepresentativePhoneNumber(),
                                        claimantType.getRepresentativeMobileNumber())
                                .filter(Objects::nonNull)
                                .toList())
                .custodyStatus("A")
                .build();
    }

    private static String getFirstName(String name) {
        if (name.contains(" ")) {
            return name.substring(0, name.indexOf(' '));
        }
        return name;
    }

    private static String getLastName(String name) {
        if (name.contains(" ")) {
            return name.substring(name.indexOf(' ') + 1);
        }
        return name;
    }
}

