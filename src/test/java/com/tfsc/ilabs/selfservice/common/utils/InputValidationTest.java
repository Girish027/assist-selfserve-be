package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.exception.InputValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;


public class InputValidationTest {

    @Test(expected = InputValidationException.class)
    public void testInputValidation() throws IOException {
        String[] testInputs = {"{\"name\":\"test-default-account-default-queue-aatest\",\"label\":\"API-Queue2\"}","{\"name\":\"test-default-account-default-queue-aatest\",\"label\":\"API-Queue2$\"}"};
        String keys = "name,label,searchKey";

        InputValidator.isInvalidInput(getJsonNode(testInputs[1]),keys,null);
    }

    @Test
    public void testInputValidationForValidInput() throws IOException {
        String[] testInputs = {"{\"name\":\"test-default-account-default-queue-aatest\",\"label\":\"API-Queue2\"}","{\"name\":\"test-default-account-default-queue-aatest\",\"label\":\"API-Queue2@\"}"};
        String keys = "name,label,searchKey";

        InputValidator.isInvalidInput(getJsonNode(testInputs[0]),keys,null);
    }

    private JsonNode getJsonNode(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }
}
