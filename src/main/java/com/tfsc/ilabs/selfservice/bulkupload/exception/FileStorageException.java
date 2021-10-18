package com.tfsc.ilabs.selfservice.bulkupload.exception;

import com.tfsc.ilabs.selfservice.common.exception.ResourceException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import org.springframework.http.HttpStatus;

public class FileStorageException extends ResourceException {

    public FileStorageException(ErrorObject error){
        super(error);
        setStatus();
    }
    private void setStatus() {
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
