package uk.gov.hmcts.reform.et.model.hearing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import uk.gov.hmcts.et.common.model.hmc.CaseDetails;
import uk.gov.hmcts.et.common.model.hmc.HearingDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.et.common.model.hmc.RequestDetails;
import uk.gov.hmcts.et.common.model.hmc.hearing.HearingResponse;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingGetResponse {

    @NonNull
    private RequestDetails requestDetails;

    @NonNull
    private HearingDetails hearingDetails;

    @NonNull
    private CaseDetails caseDetails;

    @NonNull
    private List<PartyDetails> partyDetails;

    @NonNull
    private HearingResponse hearingResponse;
}
