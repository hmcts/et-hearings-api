package uk.gov.hmcts.reform.et.utils;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;

import java.util.ArrayList;
import java.util.List;

public final class CaseFlagsHearingsUtils {

    private CaseFlagsHearingsUtils() {
        // NO-OP
    }

    public static List<CaseFlagsType> getAllActiveFlags(CaseData caseData) {
        List<CaseFlagsType> nonEmptyFlags = new ArrayList<>();
        collectNonEmptyPartyFlags(caseData, nonEmptyFlags);
        return nonEmptyFlags;
    }

    private static void collectNonEmptyPartyFlags(CaseData caseData, List<CaseFlagsType> nonEmptyFlags) {
        findNonEmptyFlags(nonEmptyFlags, caseData.getRespondentFlags());
        findNonEmptyFlags(nonEmptyFlags, caseData.getClaimantFlags());
        findNonEmptyFlags(nonEmptyFlags, caseData.getCaseFlags());
    }

    private static void findNonEmptyFlags(List<CaseFlagsType> nonEmptyFlags, CaseFlagsType flags) {
        if (flags != null && flags.getDetails() != null) {
            CaseFlagsType nonEmptyFlag = CaseFlagsType.builder()
                .partyName(flags.getPartyName())
                .roleOnCase(flags.getRoleOnCase())
                .details(flags.getDetails())
                .build();
            nonEmptyFlags.add(nonEmptyFlag);
        }
    }
}
