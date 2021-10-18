package com.tfsc.ilabs.selfservice.common.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfile {
    String name;
    String email;
    String userName;
    String access;
    String oktaUrl;
    JsonNode clients;
}

