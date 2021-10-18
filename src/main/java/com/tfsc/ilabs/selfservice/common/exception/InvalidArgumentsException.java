package com.tfsc.ilabs.selfservice.common.exception;

import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import org.springframework.http.HttpStatus;

/**
 * Created by ravi.b on 31-05-2019.
 */
public class InvalidArgumentsException extends ResourceException {

    public InvalidArgumentsException(ErrorObject object) {
        super(object);
        setStatus();
    }

    private void setStatus() {
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
