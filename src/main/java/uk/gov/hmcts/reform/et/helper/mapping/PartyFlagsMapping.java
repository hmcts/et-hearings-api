package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.et.model.service.YesNo.isYes;
import static uk.gov.hmcts.reform.et.model.service.hearingvalues.PartyFlagsMap.IS_CONFIDENTIAL_CASE;
import static uk.gov.hmcts.reform.et.model.service.hearingvalues.PartyFlagsMap.LANGUAGE_INTERPRETER_FLAG;

public final class PartyFlagsMapping {

    private PartyFlagsMapping() {

    }

    public static List<PartyFlags> getPartyFlags(CaseData caseData) {
        return Stream.of(
            confidentialCase(caseData),
            getLanguageInterpreterFlag(caseData)
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static PartyFlags confidentialCase(CaseData caseData) {
        PartyFlags confidentialCaseFlag = null;
        if (isYes(caseData.getCaseRestrictedFlag())) {
            confidentialCaseFlag = PartyFlags.builder()
                .flagId(IS_CONFIDENTIAL_CASE.getFlagId())
                .flagDescription(IS_CONFIDENTIAL_CASE.getFlagDescription())
                .flagParentId(IS_CONFIDENTIAL_CASE.getParentId())
                .build();
        }
        return confidentialCaseFlag;
    }

    public static PartyFlags getLanguageInterpreterFlag(CaseData caseData) {
        PartyFlags adjournCasePartyFlag = null;

        if (HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData)) {
            adjournCasePartyFlag = PartyFlags.builder()
                .flagId(LANGUAGE_INTERPRETER_FLAG.getFlagId())
                .flagDescription(LANGUAGE_INTERPRETER_FLAG.getFlagDescription())
                .flagParentId(LANGUAGE_INTERPRETER_FLAG.getParentId()).build();
        }
        return adjournCasePartyFlag;
    }

    //    public static PartyFlags signLanguage(CaseData caseData) {
    //        String signLanguageType = Optional
    //            .ofNullable(caseData.getParties())
    //            .map(HearingOptions::getSignLanguageType).orElse(null);
    //        PartyFlags partyFlagsSignLanguage = null;
    //        if (isNotBlank(signLanguageType)) {
    //            partyFlagsSignLanguage = PartyFlags.builder()
    //                .flagId(SIGN_LANGUAGE_TYPE.getFlagId())
    //                .flagDescription(SIGN_LANGUAGE_TYPE.getFlagDescription())
    //                .flagParentId(SIGN_LANGUAGE_TYPE.getParentId())
    //                .build();
    //        }
    //        return partyFlagsSignLanguage;
    //    }

    public static CaseFlags getCaseFlags(CaseData caseData) {
        return CaseFlags.builder()
            .flags(PartyFlagsMapping.getPartyFlags(caseData).stream()
                       .filter(Objects::nonNull)
                       .collect(Collectors.toList()))
            .flagAmendUrl("") //TODO Implement when present
            .build();
    }
}

