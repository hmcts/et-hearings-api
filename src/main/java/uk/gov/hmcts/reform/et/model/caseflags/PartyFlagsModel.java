package uk.gov.hmcts.reform.et.model.caseflags;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PartyFlagsModel extends CaseFlagsType {

    private final String partyId;

    public static CaseFlagsType from(CaseFlagsType flags) {
        return CaseFlagsType.builder()
            .partyName(flags.getPartyName())
            .details(flags.getDetails())
            .roleOnCase(flags.getRoleOnCase())
            .build();
    }
}


