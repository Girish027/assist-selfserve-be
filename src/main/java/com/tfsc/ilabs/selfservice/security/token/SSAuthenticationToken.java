package com.tfsc.ilabs.selfservice.security.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by ravi.b on 30/07/2019.
 */
@Data
@EqualsAndHashCode
public class SSAuthenticationToken extends AbstractAuthenticationToken {
    private String userId;

    private String userName;

    private String accessToken;

    private Collection<String> clientIds;

    public SSAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
