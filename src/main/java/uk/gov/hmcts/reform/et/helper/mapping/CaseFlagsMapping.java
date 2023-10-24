package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.FlagDetailType;
import uk.gov.hmcts.et.common.model.ccd.items.GenericTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.et.utils.CaseFlagsHearingsUtils.getAllActiveFlags;

public final class CaseFlagsMapping {

    private static final String ACTIVE_STATUS = "Active";

    private CaseFlagsMapping() {
        //NO-OP
    }

    public static CaseFlags getCaseFlags(CaseData caseData) {
        List<CaseFlagsType> allActiveFlags = getAllActiveFlags(caseData);

        if (allActiveFlags.isEmpty()) {
            return CaseFlags.builder()
                .flags(List.of(PartyFlags.builder()
                                   .build()))
                .build();
        }

        List<PartyFlags> partyFlagsModelList = new ArrayList<>();

        for (CaseFlagsType activeFlag : allActiveFlags) {
            String partyName = activeFlag.getPartyName();
            if (activeFlag.getDetails() != null) {
                for (GenericTypeItem<FlagDetailType> flagDetail : activeFlag.getDetails()) {
                    PartyFlags partyFlagModel = PartyFlags.builder()
                        .flagId(flagDetail.getId())
                        .partyName(partyName)
                        .flagParentId("")
                        .flagId(flagDetail.getValue().getFlagCode())
                        .flagDescription(flagDetail.getValue().getName())
                        .flagStatus(ACTIVE_STATUS)
                        .build();
                    partyFlagsModelList.add(partyFlagModel);
                }
            }
        }

        return CaseFlags.builder()
            .flags(partyFlagsModelList)
            .flagAmendUrl("")
            .build();
    }
}

