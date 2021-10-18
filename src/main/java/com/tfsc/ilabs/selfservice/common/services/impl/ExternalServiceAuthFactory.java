package com.tfsc.ilabs.selfservice.common.services.impl;

import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tfsc.ilabs.selfservice.common.services.impl.AssistExternalAuthTokenBuilder.SERVICE_ASSIST;

/**
 * <p>Factory to return Required JWTBuilder bean for Service</p>
 * <br>
 * @author Sushil.Kumar
 */
@Service
public class ExternalServiceAuthFactory {

    @Autowired
    private ExternalAuthBuilder assistExternalAuthTokenBuilder;

    public ExternalAuthBuilder getBuilder(String serviceId){
        switch (serviceId){
            case SERVICE_ASSIST:
                return assistExternalAuthTokenBuilder;
            default:
                return null;
        }
    }
}
