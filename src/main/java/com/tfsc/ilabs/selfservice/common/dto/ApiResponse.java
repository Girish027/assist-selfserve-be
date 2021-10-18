package com.tfsc.ilabs.selfservice.common.dto;

import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author Sushil.Kumar
 * <p>
 * When used in ScriptUtils.invokeFunction which is in js, usage of the following three types are supported:
 * new ApiResponse("ERROR", "Duplicate entities found", "423")
 * new ApiResponse("ERROR", "Duplicate entities found")
 * new ApiResponse("ERROR", "423")
 */
@Data
@EqualsAndHashCode
public class ApiResponse {
    private PublishState status;
    private String message;
    private HttpStatus httpCode;

    public ApiResponse() {
    }

    public ApiResponse(PublishState status, HttpStatus httpCode) {
        this.status = status;
        this.httpCode = httpCode;
    }

    public ApiResponse(PublishState status, String message) {
        this.status = status;
        this.message = message;
    }
    public String toString() {
        return "{\"status\":\"" + status + "\",\"message\":\"" + message + "\",\"httpCode\":" + httpCode + "}";
    }
}
