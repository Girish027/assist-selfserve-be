package com.tfsc.ilabs.selfservice.common.exception;

import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import org.springframework.http.HttpStatus;

/**
 * Created by ravi.b on 31-05-2019.
 */
public class NoSuchResourceException extends ResourceException {

    public NoSuchResourceException(Class cls, Object obj) {
        this("No such {0} {1}", cls.getSimpleName(), obj);
    }

    public NoSuchResourceException(String s, Object... arguments) {
        this(s, null, arguments);
    }

    public NoSuchResourceException(ErrorObject object) {
        super(object);
        setStatus();
    }

    public NoSuchResourceException(String s, Throwable ex, Object... arguments) {
        super(s, ex, arguments);
        setStatus();
    }

    private void setStatus() {
        setHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
