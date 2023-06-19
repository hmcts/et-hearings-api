package uk.gov.hmcts.reform.et.exception;

public class CaseException extends Exception {

    private static final long serialVersionUID = 42L;

    public CaseException(String message) {
        super(message);
    }
}
