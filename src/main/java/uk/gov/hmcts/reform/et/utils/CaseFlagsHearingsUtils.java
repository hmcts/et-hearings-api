package uk.gov.hmcts.reform.et.utils;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;
import uk.gov.hmcts.reform.et.model.caseflags.PartyFlagsModel;

import java.util.ArrayList;
import java.util.List;

public class CaseFlagsHearingsUtils {

    private CaseFlagsHearingsUtils() {
        //NO-OP
    }

    private static final String SPECIAL_MEASURES_FLAG_CODE = "SM";
    private static final String REASONABLE_ADJUSTMENTS_FLAG_CODE = "RA";
    private static final String DETAINED_INDIVIDUAL_FLAG_CODE = "PF0019";

    public static List<PartyFlagsModel> getAllActiveFlags(CaseData caseData) {
        List<PartyFlagsModel> nonEmptyFlags = new ArrayList<>();

        getNonEmptyPartyFlags(caseData, nonEmptyFlags);
        findActiveFlags(nonEmptyFlags);

        return nonEmptyFlags;
    }

    private static void getNonEmptyPartyFlags(CaseData caseData, List<PartyFlagsModel> nonEmptyFlags) {
        findNonEmptyFlags(nonEmptyFlags, caseData.getClaimantFlags());
        findNonEmptyFlags(nonEmptyFlags, caseData.getRespondentFlags());
    }

    private static void findNonEmptyFlags(List<PartyFlagsModel> nonEmptyFlags, CaseFlagsType flags) {
        if (flags != null
            && flags.getDetails() != null) {
            nonEmptyFlags.add(PartyFlagsModel.from(flags).toBuilder().partyID(partyID).build());
        }
    }

    private static void findNonEmptyFlags(List<PartyFlagsModel> nonEmptyFlags, CaseFlagsType flags, String partyId) {
        if (flags != null
            && flags.getDetails() != null) {
            nonEmptyFlags.add(PartyFlagsModel.from(flags).toBuilder().partyID(partyId).build());
        }
    }

    private static void findActiveFlags(List<PartyFlagsModel> nonEmptyFlags) {
        if (nonEmptyFlags != null && !nonEmptyFlags.isEmpty()) {
            nonEmptyFlags.forEach(f -> f.getDetails().removeIf(d -> !d.getValue().getStatus().equals("Active")));
            nonEmptyFlags.forEach(f -> f.getFlagStatus().equals("Active"));
        }
    }
}

