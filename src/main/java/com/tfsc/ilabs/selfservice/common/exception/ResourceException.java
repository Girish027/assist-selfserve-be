package com.tfsc.ilabs.selfservice.common.exception;

import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ravi.b on 31-05-2019.
 */
public class ResourceException extends SelfServeException {
    private final Map<String, Object> propertyErrorObject;
    private HttpStatus httpStatus;

    public ResourceException(Map<String, Object> propertyErrorObject) {
        super("");
        this.propertyErrorObject = propertyErrorObject;
    }

    public ResourceException(ErrorObject object) {
        super(object.getMessage(), object.getData());
        Map<String, Object> propertyErr = new HashMap<>();
        propertyErr.put(Constants.MESSAGE, MessageFormat.format(object.getMessage(), object.getData()));
        if (object.getData() != null) {
            propertyErr.put(Constants.DATA, object.getData());
        }
        this.propertyErrorObject = propertyErr;
    }

    ResourceException(String s, Throwable ex, Object... arguments) {
        super(s, ex, arguments);
        this.propertyErrorObject = null;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, Object> getPropertyErrors() {
        return propertyErrorObject;
    }
}
