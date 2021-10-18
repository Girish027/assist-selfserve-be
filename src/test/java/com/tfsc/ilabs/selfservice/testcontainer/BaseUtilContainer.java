package com.tfsc.ilabs.selfservice.testcontainer;

public class BaseUtilContainer {
    private BaseUtilContainer() {
        // hiding the implicit public constructor
    }

    public final static String nestedJsonObject1 = "{\"num\":1,\"obj\":{\"nest1\":{\"nest1_num\":2,\"nest11\":{\"nest11_alpha\":\"GHI\",\"nest11_num\":3}}},\"arr\":[{\"two\":\"2\"}],\"arr2\":[{\"one\":\"1\"},{\"two\":\"2\"}]}";
    public final static String nestedJsonObject2 = "{\"alpha\":\"ZYX\",\"obj\":{\"nest1\":{\"nest2_alpha\":\"WVU\",\"nest21\":{\"nest21_alpha\":\"TSR\",\"nest2_num\":7}}},\"arr\":[3],\"arr2\":[{\"three\":\"3\"},{\"two\":\"5\"}]}";
    public final static String mergedJsonObject = "{\"num\":1,\"obj\":{\"nest1\":{\"nest1_num\":2,\"nest11\":{\"nest11_alpha\":\"GHI\",\"nest11_num\":3},\"nest2_alpha\":\"WVU\",\"nest21\":{\"nest21_alpha\":\"TSR\",\"nest2_num\":7}}},\"arr\":[{\"two\":\"2\"},3],\"arr2\":[{\"one\":\"1\",\"three\":\"3\"},{\"two\":\"2\"}],\"alpha\":\"ZYX\"}";
    public final static String nestedJsonArray1 = "[{\"num\":1,\"arr\":[2]}]";
    public final static String nestedJsonArray2 = "[{\"alpha\":\"ZYX\",\"arr\":[3]}]";
    public final static String mergedJsonArray = "[{\"num\":1,\"arr\":[2]},{\"alpha\":\"ZYX\",\"arr\":[3]}]";
}
