package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StringUtils {

    private static final Logger logger =
            LoggerFactory.getLogger(StringUtils.class);

    private StringUtils() {
        // hide implicit public constructor
    }

    public static boolean isKeyAList(String key) {
        return key.length() >= 3 && key.charAt(key.length() - 3) == '[' && key.charAt(key.length() - 1) == ']';
    }

    public static String valueAsString(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("Error converting data to String", e);
        }
        return "";
    }

    public static JsonNode valueAsJsonNode(Object data) {
        try {
            return data != null ? new ObjectMapper().readTree(data.toString()) : null;
        } catch (IOException e) {
            logger.error("Error while converting object value to JsonNode", e);
        }
        return null;
    }

    /**
     * Convert String value into Java object
     * @param data
     * @param className
     * @param <T>
     * @return
     */
    public static <T> T valueAsObject(String data, Class<T> className) {
        try {
            return data != null ? new ObjectMapper().readValue(data, className) : null;
        } catch (Exception e) {
            logger.error("Error while converting object value to Reference {}", className, e);
        }
        return null;
    }
}
