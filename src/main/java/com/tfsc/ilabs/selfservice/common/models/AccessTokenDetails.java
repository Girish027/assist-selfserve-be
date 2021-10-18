package com.tfsc.ilabs.selfservice.common.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDetails implements Serializable {

    @JsonProperty("accessToken")
    @JsonAlias("access_token")
    private String accessToken;

    @JsonProperty("refreshToken")
    @JsonAlias("refresh_token")
    private boolean refresh_token;

    @JsonProperty("idToken")
    @JsonAlias("id_token")
    private String idToken;

    @JsonProperty("tokenType")
    @JsonAlias("token_type")
    private String tokenType;

    @JsonProperty("expiresIn")
    @JsonAlias("expires_in")
    private Integer expiresIn;
}
