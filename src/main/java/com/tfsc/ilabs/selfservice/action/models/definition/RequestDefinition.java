package com.tfsc.ilabs.selfservice.action.models.definition;

import io.swagger.models.HttpMethod;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ravi.b on 24-07-2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestDefinition {
    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    private Map<String, String> headers;

    private String url;

    private String body;

}
