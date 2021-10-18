package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Created by ravi.b on 11-06-2019.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityContextUtil.getCurrentUserName());
    }
}