package com.tfsc.ilabs.selfservice.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TfsEnvVariables {
    String docLink;
    String supportLink;
    String toolRoot;
    String toolName;
    String toolTitle;
    String toolType;
    String env;
    String oktaUrl;
    String serviceBaseUrl;
    String parentBaseUrl;
    String cssOktaEnabled;

}
