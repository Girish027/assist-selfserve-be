package com.tfsc.ilabs.selfservice.common.exception;
/**
 * Created by ravi.b on 31-05-2019.
 */
public class InternalException extends SelfServeException {

    public InternalException(String s, Object... arguments) {
        super(s, arguments);
    }

    public InternalException(String s, Throwable ex, Object... arguments) {
        super(s, ex, arguments);
    }
}
