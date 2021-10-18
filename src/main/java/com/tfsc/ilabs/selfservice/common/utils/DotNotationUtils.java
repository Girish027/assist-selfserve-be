package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.tfsc.ilabs.selfservice.common.exception.InvalidArgumentsException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;

import java.io.IOException;
import java.util.Map;

public class DotNotationUtils {

    protected static JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    private DotNotationUtils() {
        // hiding public implicit constructor
    }

    /**
     * This methods takes a dot notation rule and add the primitive value to the
     * specified json object
     *
     * @param dottedLocation Dot notation of path to which a value needs to be
     *                       added. eg in json object person path dottedLocation can
     *                       be contact.phone
     * @param value          value which needs to be added in the object it should
     *                       be a JsonNode (IntNode , BooleanNode , TextNode ,
     *                       ObjectNode or ArrayNode)
     * @param obj            json object to which data needs to be appended
     * @version 1.0
     */
    public static void addToJsonObject(String dottedLocation, JsonNode value, JsonNode obj) {
        if (obj == null) {
            obj = new ObjectMapper().createObjectNode();
        }

        String[] properties = dottedLocation.split("\\.");

        for (int i = 0; i < properties.length; i++) {
            if (i == properties.length - 1) {
                if (isArrayProperty(properties[i])) {
                    String key = getKeyFromArryProperty(properties[i]); // removing the square brackets
                    ArrayNode arrayNode = obj.get(key) == null ? jsonNodeFactory.arrayNode() : (ArrayNode) obj.get(key);
                    arrayNode.add(value);
                    ((ObjectNode) obj).set(key, arrayNode);
                } else {
                    ((ObjectNode) obj).set(properties[i], value);
                }
                break;
            }
            if (obj.get(properties[i]) == null) {
                ((ObjectNode) obj).set(properties[i], jsonNodeFactory.objectNode());
            }
            obj = obj.get(properties[i]);
        }
    }

    public static void addToJsonObject(String dottedLocation, String value, ObjectNode obj) {
        addToJsonObject(dottedLocation, TextNode.valueOf(value), obj);
    }

    public static void addToJsonObject(String dottedLocation, Integer value, ObjectNode obj) {
        addToJsonObject(dottedLocation, IntNode.valueOf(value), obj);
    }

    public static void addToJsonObject(String location, Boolean value, ObjectNode obj) {
        addToJsonObject(location, BooleanNode.valueOf(value), obj);
    }

    /**
     * This methods takes Set of locations(dot notation) and value and create a JSON
     * object from these
     *
     * @param dotted These are set of location and values which needs to be combined
     *               to form a json object
     * @return ObjectNode This is a json object created with the set of dottend
     * notation provided
     */
    public static ObjectNode createJsonObject(Map<String, JsonNode> dotted) {
        ObjectNode objectNode = jsonNodeFactory.objectNode();

        for (Map.Entry<String, JsonNode> entry : dotted.entrySet()) {
            addToJsonObject(entry.getKey(), entry.getValue(), objectNode);
        }
        return objectNode;
    }

    /**
     * This methods will retreive a value specified at certain location(provided by
     * dottedLocation) in json object
     *
     * @param dottedLocation This is the location from where the value need to be
     *                       retrieved from the json object
     * @param json           This is the json object in string representation from
     *                       which one of the value needs to be retrieved.
     * @return Object value of present at specified location
     */

    public static JsonNode getValueFromJson(String dottedLocation, String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = null;
        JsonNode obj = null;
        try {
            parser = factory.createParser(json);
            obj = mapper.readTree(parser);
        } catch (IOException e) {
            throw new InvalidArgumentsException(new ErrorObject("Unable to parse provided json", json));
        }
        return getValueFromJson(dottedLocation, obj);
    }

    static JsonNode getValueFromJson(String dottedLocation, JsonNode obj) {
        JsonNode value = null;
        if (!dottedLocation.equals("")) {
            String[] properties = dottedLocation.split("\\.");
            value = getValueFromJson(0, properties, obj);
        }
        return value;
    }

    private static JsonNode getValueFromJson(int startIndex, String[] properties, JsonNode obj) {
        JsonNode value = obj;
        JsonNode latestObj = obj;
        if (properties.length > startIndex) {
            String property = properties[startIndex];
            if (isArrayProperty(property)) {
                int index = getIndexFromArrayProperty(property);
                String arrayKey = getKeyFromArryProperty(property);
                if (arrayKey != null && !arrayKey.equals("")) {
                    latestObj = latestObj.get(arrayKey);
                }
                if (index == -2) {
                    // Create a new ArrayNode and add values to it
                    value = new ObjectMapper().createArrayNode();
                    for (int iter = 0; iter < latestObj.size(); iter++) {
                        ((ArrayNode) value).add(getValueFromJson(startIndex + 1, properties, latestObj.get(iter)));
                    }
                } else {
                    value = getValueFromJson(startIndex + 1, properties, latestObj.get(index));
                }
            } else {
                if (properties.length > startIndex + 1) {
                    value = getValueFromJson(startIndex + 1, properties, latestObj.get(property));
                } else {
                    value = latestObj.get(property);
                }
            }
        }
        return value;
    }

    /**
     * The assumption is that an array property will be of the form
     * propertyName[index]
     *
     */
    private static boolean isArrayProperty(String property) {
        return property.endsWith("]") && property.contains("[");
    }

    private static String getKeyFromArryProperty(String property) {
        return property.substring(0, property.indexOf('['));
    }

    private static Integer getIndexFromArrayProperty(String property) {
        String index = property.substring(property.indexOf('[') + 1, property.length() - 1);
        if (index.equals("*")) {
            // Return -2 to signify all indices
            return -2;
        } else {
            try {
                return Integer.parseInt(index);
            } catch (Exception e) {
                // Signify invalid index
                return -1;
            }
        }
    }
}