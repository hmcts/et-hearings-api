package uk.gov.hmcts.reform.et.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private Integer code;
}
