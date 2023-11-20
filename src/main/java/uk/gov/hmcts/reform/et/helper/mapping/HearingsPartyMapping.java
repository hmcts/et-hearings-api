package uk.gov.hmcts.reform.et.helper.mapping;

import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.FlagDetailType;
import uk.gov.hmcts.et.common.model.ccd.items.GenericTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;
import uk.gov.hmcts.et.common.model.ccd.types.ClaimantIndType;
import uk.gov.hmcts.et.common.model.ccd.types.ClaimantType;
import uk.gov.hmcts.et.common.model.ccd.types.Organisation;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeC;
import uk.gov.hmcts.et.common.model.ccd.types.RepresentedTypeR;
import uk.gov.hmcts.et.common.model.ccd.types.RespondentSumType;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.OrganisationDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.et.common.model.hmc.UnavailabilityRanges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.gov.hmcts.ecm.common.model.helper.CaseFlagConstants.LANGUAGE_INTERPRETER;
import static uk.gov.hmcts.ecm.common.model.helper.CaseFlagConstants.SIGN_LANGUAGE_INTERPRETER;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.CLAIMANT;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.LEGAL_REPRESENTATIVE;
import static uk.gov.hmcts.reform.et.model.hmc.reference.EntityRoleCode.RESPONDENT;

public final class HearingsPartyMapping {

    public static final String INDIVIDUAL = "IND";
    public static final String ORGANISATION = "ORG";
    public static final String VULNERABLE_USER = "PF0002";

    private HearingsPartyMapping() {

    }

    /**
     * Construct a list of PartyDetails for HMC. Maps Claimant, Respondent and their legal reps where appropriate.
     */
    public static List<PartyDetails> buildPartyObjectForHearingPayload(CaseData caseData) {
        List<PartyDetails> parties = new ArrayList<>();
        parties.add(mapPartyDetailsForClaimant(caseData));

        ofNullable(caseData.getRepresentativeClaimantType())
                .filter(claimant -> isNotEmpty(claimant.getNameOfRepresentative()))
                .ifPresent(claimant -> parties.addAll(mapPartyDetailsForClaimantRep(caseData)));

        ofNullable(caseData.getRespondentCollection())
                .ifPresent(respondents -> parties.addAll(respondents.stream()
                    .map(o -> mapPartyDetailsForRespondent(o, caseData))
                    .toList()));

        ofNullable(caseData.getRepCollection())
                .ifPresent(representatives -> parties.addAll(representatives.stream()
                        .flatMap(HearingsPartyMapping::mapPartyDetailsForRespondentRep)
                        .toList()));

        return parties;
    }

    private static PartyDetails mapPartyDetailsForRespondent(RespondentSumTypeItem respondentItem, CaseData caseData) {
        RespondentSumType respondent = respondentItem.getValue();
        assert respondent != null;

        PartyDetails details = PartyDetails.builder()
                .partyID(respondentItem.getId())
                .partyRole(RESPONDENT.getHmcReference())
                .partyType(ORGANISATION)
                .partyName(respondent.getRespondentOrganisation())
                .unavailabilityRanges(mapCcdUnavailabilityToHmc(caseData.getRespondentUnavailability()))
                .build();

        if (isNotEmpty(details.getPartyName())) {
            details.setOrganisationDetails(OrganisationDetails.builder()
                    .name(respondent.getRespondentOrganisation())
                    .organisationType(ORGANISATION)
                    .build());

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

        FlagDetailType vulnerableFlag = getVulnerableFlag(caseData.getRespondentFlags());

        details.setIndividualDetails(IndividualDetails.builder()
                .title(respondent.getEt3ResponseRespondentPreferredTitle())
                .interpreterLanguage(getInterpreterLanguage(caseData.getRespondentFlags()))
                .vulnerableFlag(vulnerableFlag != null)
                .vulnerabilityDetails(ofNullable(vulnerableFlag)
                        .map(FlagDetailType::getFlagComment)
                        .orElse(null))
                .firstName(firstName)
                .lastName(lastName)
                .build());

        return details;
    }

    private static Stream<PartyDetails> mapPartyDetailsForRespondentRep(RepresentedTypeRItem repItem) {
        RepresentedTypeR rep = repItem.getValue();

        PartyDetails indPartyDetails = PartyDetails.builder()
                .partyID(repItem.getId())
                .partyName(rep.getNameOfRepresentative())
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(INDIVIDUAL)
                .individualDetails(IndividualDetails.builder()
                        .firstName(getFirstName(rep.getNameOfRepresentative()))
                        .lastName(getLastName(rep.getNameOfRepresentative()))
                        .build())
                .build();

        Organisation organisation = rep.getRespondentOrganisation();
        PartyDetails firmPartyDetails = PartyDetails.builder()
                .partyID(ofNullable(rep.getRespondentOrganisation())
                        .map(Organisation::getOrganisationID)
                        .orElse(defaultString(rep.getNonMyHmctsOrganisationId(), UUID.randomUUID().toString())))
                .partyName(defaultString(rep.getNameOfOrganisation(), rep.getNameOfRepresentative()))
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(ORGANISATION)
                .organisationDetails(OrganisationDetails.builder()
                        .name(rep.getNameOfOrganisation())
                        .cftOrganisationID(ObjectUtils.isEmpty(organisation) ? null : organisation.getOrganisationID())
                        .organisationType(ORGANISATION)
                        .build())
                .build();

        return Stream.of(indPartyDetails, firmPartyDetails);
    }

    private static PartyDetails mapPartyDetailsForClaimant(CaseData caseData) {
        ClaimantIndType claimantIndType = caseData.getClaimantIndType();
        ClaimantType claimant = caseData.getClaimantType();
        FlagDetailType vulnerableFlag = getVulnerableFlag(caseData.getClaimantFlags());
        IndividualDetails individualDetails = IndividualDetails.builder()
                .title(defaultString(claimantIndType.getClaimantTitle(), claimantIndType.getClaimantPreferredTitle()))
                .firstName(claimantIndType.getClaimantFirstNames())
                .lastName(claimantIndType.getClaimantLastName())
                .hearingChannelPhone(Stream.of(claimant.getClaimantPhoneNumber(), claimant.getClaimantMobileNumber())
                        .filter(Objects::nonNull)
                        .toList())
                .hearingChannelEmail(Stream.of(claimant.getClaimantEmailAddress()).filter(Objects::nonNull).toList())
                .preferredHearingChannel(claimant.getClaimantContactPreference())
                .interpreterLanguage(getInterpreterLanguage(caseData.getClaimantFlags()))
                .vulnerableFlag(vulnerableFlag != null)
                .vulnerabilityDetails(ofNullable(vulnerableFlag).map(FlagDetailType::getFlagComment).orElse(null))
                .custodyStatus("A")
                .build();

        return PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(caseData.getClaimantId()))
                .partyID(defaultString(caseData.getClaimantId(), UUID.randomUUID().toString()))
                .partyName(caseData.getClaimant())
                .partyRole(CLAIMANT.getHmcReference())
                .partyType(INDIVIDUAL)
                .individualDetails(individualDetails)
                .unavailabilityRanges(mapCcdUnavailabilityToHmc(caseData.getClaimantUnavailability()))
                .build();
    }

    private static List<UnavailabilityRanges> mapCcdUnavailabilityToHmc(ListTypeItem<UnavailabilityRanges> ranges) {
        return ranges.stream().map(GenericTypeItem::getValue).toList();
    }

    private static FlagDetailType getVulnerableFlag(CaseFlagsType flags) {
        if (flags.getDetails() == null) {
            return null;
        }

        return flags.getDetails().stream()
                .map(GenericTypeItem::getValue)
                .filter(o -> VULNERABLE_USER.equals(o.getFlagCode()))
                .findFirst()
                .orElse(null);
    }

    private static String getInterpreterLanguage(CaseFlagsType flags) {
        if (flags.getDetails() == null) {
            return null;
        }

        return flags.getDetails().stream()
                .map(GenericTypeItem::getValue)
                .filter(o -> LANGUAGE_INTERPRETER.equals(o.getName()) || SIGN_LANGUAGE_INTERPRETER.equals(o.getName()))
                .findFirst()
                .map(FlagDetailType::getSubTypeKey)
                .orElse(null);
    }

    private static List<PartyDetails> mapPartyDetailsForClaimantRep(CaseData caseData) {
        RepresentedTypeC claimantType = caseData.getRepresentativeClaimantType();

        PartyDetails indPartyDetails = PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(claimantType.getRepresentativeId())
                .partyID(defaultString(claimantType.getRepresentativeId(), UUID.randomUUID().toString()))
                .partyName(claimantType.getNameOfRepresentative())
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(INDIVIDUAL)
                .individualDetails(mapIndividualDetailsForClaimantRep(claimantType))
                .build();

        PartyDetails orgPartyDetails = PartyDetails.builder()
                // TODO: Remove the defaultString line when migration has gone live (RET-4383)
                // .partyID(claimantType.getOrganisationId())
                .partyID(defaultString(claimantType.getOrganisationId(), UUID.randomUUID().toString()))
                .partyName(defaultString(claimantType.getNameOfOrganisation(), claimantType.getNameOfRepresentative()))
                .partyRole(LEGAL_REPRESENTATIVE.getHmcReference())
                .partyType(ORGANISATION)
                .organisationDetails(OrganisationDetails.builder()
                        .name(claimantType.getNameOfOrganisation())
                        .organisationType(ORGANISATION)
                        .build())
                .build();

        return List.of(indPartyDetails, orgPartyDetails);
    }

    private static IndividualDetails mapIndividualDetailsForClaimantRep(RepresentedTypeC rep) {
        return IndividualDetails.builder()
                .firstName(getFirstName(rep.getNameOfRepresentative()))
                .lastName(getLastName(rep.getNameOfRepresentative()))
                .preferredHearingChannel(rep.getRepresentativePreference())
                .hearingChannelEmail(Stream.of(rep.getRepresentativeEmailAddress()).filter(Objects::nonNull).toList())
                .hearingChannelPhone(
                        Stream.of(rep.getRepresentativePhoneNumber(), rep.getRepresentativeMobileNumber())
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

