package com.tfsc.ilabs.selfservice.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * Created by ravi.b on 31-05-2019.
 */
@Getter
@Setter
public class SelfServeException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public SelfServeException(String s, Object... arguments){
        super(MessageFormat.format(s, arguments));
    }

    public SelfServeException(String s, Throwable ex, Object... arguments){
        super(MessageFormat.format(s, arguments), ex);
    }
}