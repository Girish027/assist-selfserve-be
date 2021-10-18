package com.tfsc.ilabs.selfservice.common.models;

/**
 * Created by ravi.b on 10-09-2019.
 */
public class ErrorObject{
    private String message;
    private Object data;

    public ErrorObject(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ErrorObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}