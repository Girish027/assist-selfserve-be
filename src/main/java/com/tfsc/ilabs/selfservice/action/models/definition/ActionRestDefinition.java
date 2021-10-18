package com.tfsc.ilabs.selfservice.action.models.definition;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ravi.b on 04-06-2019.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ActionRestDefinition extends ActionDefinition {

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private HttpMethod method;

    @NotNull
    private Map<String, String> headers;

    /**
     * This is the object definition used map different field of workflow data to object which will be sent in action call.
     * Cant be used  in complex cases;
     * Note : Its an alternative to "objectTranslator".
     * So either one of them will only be present with precedence given to objectTranslator
     */
    private Map<String, String> bodyDefinition;

    private String relativePath;

    private String serviceId;

    /**
     * This is the javascript function with name "translate"  and input as pageData and entity.
     * eg : function(var pageData, var entity){ // implementation}
     * This will be used to translate the workflow data into object suitable for action call,
     * Note : Its an alternative to "bodyDefinition" . So either one of them will only be present with precedence given to objectTranslator
     */
    private String objectTranslator;
    /**
     * This key is used to send POST request as key value mapping for x-wwww-form-urlencoded request
     */
    private String requestBodyKey;

    @Enumerated(EnumType.STRING)
    private RestClientType restClientType;

    private JsonNode preFetchDefinition;

    private boolean idLookupRequired;

    private boolean uploadFile;

    private String idLookupKeys;

    private boolean prefetchRequiredForLiveOnly;

    private boolean getPathParamFromPageData;

    private boolean urlParamsEncoded;

    public ActionRestDefinition() {
        //for jackson lib
        method = HttpMethod.GET;
        headers = new HashMap<>();
    }

}
