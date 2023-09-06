package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.FlagDetailType;
import uk.gov.hmcts.et.common.model.ccd.items.GenericTypeItem;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;
import uk.gov.hmcts.reform.et.model.caseflags.PartyFlagsModel;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.et.utils.CaseFlagsHearingsUtils.getAllActiveFlags;

public class CaseFlagsMapping {

    private static final String ACTIVE_STATUS = "Active";

    private CaseFlagsMapping() {
        //NO-OP
    }

    public static CaseFlags getCaseFlags(CaseData caseData) {
        List<PartyFlagsModel> allActiveFlags = getAllActiveFlags(caseData);

        if (allActiveFlags.isEmpty()) {
            return CaseFlags.builder()
                .flags(List.of(PartyFlags.builder()
                                   .build()))
                .build();
        }

        List<PartyFlags> partyFlagsModelList = new ArrayList<>();

        for (PartyFlagsModel activeFlag : allActiveFlags) {
            String partyName = activeFlag.getPartyName();
            for (GenericTypeItem<FlagDetailType> flagDetail : activeFlag.getDetails()) {
                PartyFlags partyFlagModel = PartyFlags.builder()
                    .partyId(activeFlag.getPartyId())
                    .partyName(partyName)
                    .flagParentId("")
                    .flagId(flagDetail.getValue().getFlagCode())
                    .flagDescription(flagDetail.getValue().getName())
                    .flagStatus(ACTIVE_STATUS)
                    .build();
                partyFlagsModelList.add(partyFlagModel);
            }
        }

        return CaseFlags.builder()
            .flags(partyFlagsModelList)
            .build();
    }
}

