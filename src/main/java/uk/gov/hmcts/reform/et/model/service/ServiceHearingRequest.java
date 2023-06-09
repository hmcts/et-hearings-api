package uk.gov.hmcts.reform.et.model.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ServiceHearingRequest {
    @NonNull
    @JsonProperty("caseReference")
    private String caseId;
    private String hearingId;
}
