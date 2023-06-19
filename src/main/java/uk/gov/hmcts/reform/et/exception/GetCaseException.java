package uk.gov.hmcts.reform.et.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class GetCaseException extends CaseException  {

    private static final long serialVersionUID = -3977477574548786807L;

    public GetCaseException(String message) {
        super(message);
    }
}
