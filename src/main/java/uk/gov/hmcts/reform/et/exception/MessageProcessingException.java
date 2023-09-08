package uk.gov.hmcts.reform.et.exception;

import java.io.Serial;

public class MessageProcessingException extends Exception {
    @Serial
    private static final long serialVersionUID = -1216639664216596788L;

    public MessageProcessingException(String message) {
        super(message);
    }
}
