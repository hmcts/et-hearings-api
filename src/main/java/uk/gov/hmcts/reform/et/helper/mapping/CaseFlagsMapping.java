package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CaseFlagsMapping {
    private CaseFlagsMapping() {

    }

    /**
     * Flattens case and party flags into a single CaseFlags object for the HMC payload.
     */
    public static CaseFlags getCaseFlags(CaseData caseData) {
        List<PartyFlags> flags = new ArrayList<>();

        flags.addAll(mapCaseFlagsToPartyFlags("", caseData.getCaseFlags()));
        flags.addAll(mapCaseFlagsToPartyFlags(caseData.getClaimantId(), caseData.getClaimantFlags()));

        Optional.ofNullable(caseData.getRespondentCollection())
                .map(o -> o.get(0))
                .map(RespondentSumTypeItem::getId)
                .ifPresent(o -> flags.addAll(mapCaseFlagsToPartyFlags(o, caseData.getRespondentFlags())));

        return CaseFlags.builder()
                .flags(flags)
                .flagAmendUrl("")
                .build();
    }

    /**
     * Map CaseFlag objects into PartyFlags for HMC.
     * @param partyId ID for the party being mapped
     * @param flags CaseFlags object as recognised by CCD
     */
    private static List<PartyFlags> mapCaseFlagsToPartyFlags(String partyId, CaseFlagsType flags) {
        if (flags == null || flags.getDetails() == null) {
            return new ArrayList<>();
        }

        return flags.getDetails().stream()
                .filter(Objects::nonNull)
                .map(flag -> PartyFlags.builder()
                        .partyName(flags.getPartyName())
                        .flagDescription(flag.getValue().getName())
                        .flagId(flag.getValue().getFlagCode())
                        .flagParentId("")
                        .flagStatus(flag.getValue().getStatus())
                        .partyId(partyId)
                        .build())
                .toList();
    }
}

