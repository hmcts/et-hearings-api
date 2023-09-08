package uk.gov.hmcts.reform.et.model.hmc.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import uk.gov.hmcts.reform.et.model.hmc.reference.HmcStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HearingUpdate {

    private LocalDateTime hearingResponseReceivedDateTime;
    private LocalDateTime hearingEventBroadcastDateTime;
    @NonNull
    @JsonProperty("HMCStatus")
    private HmcStatus hmcStatus;
    private LocalDateTime nextHearingDate;
    private String listAssistSessionID;
    @JsonProperty("hearingVenueId")
    private String hearingEpimsId;
    private String hearingRoomId;
    private String hearingJudgeId;
}
