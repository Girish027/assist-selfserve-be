package com.tfsc.ilabs.selfservice.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Access Token payload information
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccessTokenInfo implements Serializable {

    private String token;
    private boolean active;
    private String scope;
    private String username;
    private long exp;
    private long iat;
    private String sub;
    private String iss;
    private String uid;
    private long createdAtMillis;

    @JsonIgnore     // prevent deserialization
    private Map<String, List<String>> roles;
    private Map<String, UserAccessTokenInfo> externalApiTokenMap;
    private List<String> clientIds;
    private AccessTokenDetails accessTokenDetails;

}
