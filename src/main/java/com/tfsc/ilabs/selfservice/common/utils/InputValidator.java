package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.exception.InputValidationException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    private InputValidator(){}

    public static void isInvalidInput(JsonNode node,String keys, String patternRegEx) throws IOException {
        String PATTERN = "[^A-Za-z0-9-_ .,:@]";
        if(patternRegEx!=null){
        PATTERN = patternRegEx;}
        String[] toSearch =keys.split(",");
        List<String> keysList = Arrays.asList(toSearch);
        Pattern pattern = Pattern.compile(PATTERN);
        boolean flag = keysList.stream().anyMatch(key->{
            Matcher match = pattern.matcher(node.findPath(key).asText());
            return match.find();
        });
        if(flag) {
            throw new InputValidationException(new ErrorObject("input values does not meet the criteria"));
        }
    }
}
