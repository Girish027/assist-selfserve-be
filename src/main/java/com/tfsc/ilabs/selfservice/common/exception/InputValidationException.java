package com.tfsc.ilabs.selfservice.common.exception;

import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import org.springframework.http.HttpStatus;

public class InputValidationException extends ResourceException {

    public InputValidationException(ErrorObject object) {
        super(object);
        setStatus();

    }
    private void setStatus() {
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
