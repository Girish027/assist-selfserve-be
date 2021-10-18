package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponseUtils {

    private ResponseUtils() {
        // hide implicit public constructor
    }

    /**
     * This method will interpret the contract and update the responseUi using the
     * values from responseApi
     */
    public static void evaluateContract(JsonNode responseApiToResponseUi, JsonNode responseApi, JsonNode responseUi) {
        // isArray is to check if the contract is an array or not
        if (responseApiToResponseUi.isArray()) {
            for (JsonNode contractJson : responseApiToResponseUi) {
                String apiRespPath = contractJson.get("apiRespPath").asText();
                String uiRespPath = contractJson.get("uiRespPath").asText();

                // Now traverse over the apiRespPath and collect the value
                JsonNode value = DotNotationUtils.getValueFromJson(apiRespPath, responseApi);

                // After receiving the target value
                // Insert target value into the target path in the reponseUi object
                DotNotationUtils.addToJsonObject(uiRespPath, value, responseUi);
            }
        }
        // TO-DO : else if part
    }
}
