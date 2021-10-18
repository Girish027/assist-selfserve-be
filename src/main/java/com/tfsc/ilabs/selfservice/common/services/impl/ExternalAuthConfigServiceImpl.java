package com.tfsc.ilabs.selfservice.common.services.impl;

import com.tfsc.ilabs.selfservice.common.models.ExternalServiceAuthConfig;
import com.tfsc.ilabs.selfservice.common.repositories.ExternalAuthConfigRepository;
import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Sushil.Kumar
 */
@Service
public class ExternalAuthConfigServiceImpl implements ExternalAuthConfigService {

    @Autowired
    private ExternalAuthConfigRepository externalAuthConfigRepository;

    @Override
    public String getValue(String name, String serviceId) {
        Optional<ExternalServiceAuthConfig> jwtConfig = externalAuthConfigRepository.findByNameAndServiceId(name, serviceId);
        if(jwtConfig.isPresent()){
            return jwtConfig.get().getValue();
        }
        return null;
    }
}
