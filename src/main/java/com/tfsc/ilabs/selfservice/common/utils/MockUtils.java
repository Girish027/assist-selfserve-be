package com.tfsc.ilabs.selfservice.common.utils;

public class MockUtils {

    private static final String UMS_ROLES_RESPONSE2 = "{\"clients\":[{\"clientId\":\"salesdemo\",\"clientDisplayName\":\"salesdemo\",\"accounts\":[{\"accountId\":\"salesdemo\",\"accountDisplayName\":\"Salesdemo\",\"packageCode\":\"custom\",\"products\":[{\"productId\":\"chat\",\"roles\":[\"developer\"],\"components\":[{\"componentId\":\"ohs\",\"componentClientId\":\"nemo-client-salesdemo\",\"componentAccountId\":\"salesdemo-account-default\"},{\"componentId\":\"assist\",\"componentClientId\":\"nemo-client-salesdemo\",\"componentAccountId\":\"salesdemo-account-default\"},{\"componentId\":\"ccc\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"tie\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ude\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"VCC\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assistops\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"wfm\",\"componentClientId\":null,\"componentAccountId\":null}]}]}]},{\"clientId\":\"marriott\",\"clientDisplayName\":\"Marriott\",\"accounts\":[{\"accountId\":\"marriott\",\"accountDisplayName\":\"Marriott\",\"packageCode\":\"custom\",\"products\":[{\"productId\":\"chat\",\"roles\":[\"developer\"],\"components\":[{\"componentId\":\"ohs\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assist\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ccc\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"tie\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ude\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"VCC\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assistops\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"wfm\",\"componentClientId\":null,\"componentAccountId\":null}]}]}]},{\"clientId\":\"selfservedemo\",\"clientDisplayName\":\"Self Serve Demo\",\"accounts\":[{\"accountId\":\"selfservedemo\",\"accountDisplayName\":\"Self Serve Demo\",\"packageCode\":\"custom\",\"products\":[{\"productId\":\"chat\",\"roles\":[\"developer\"],\"components\":[{\"componentId\":\"ohs\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assist\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ccc\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"tie\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ude\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"VCC\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assistops\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"wfm\",\"componentClientId\":null,\"componentAccountId\":null}]}]}]},{\"clientId\":\"test-default\",\"clientDisplayName\":\"Test Default\",\"accounts\":[{\"accountId\":\"test-default\",\"accountDisplayName\":\"Test Default\",\"packageCode\":\"custom\",\"products\":[{\"productId\":\"chat\",\"roles\":[\"admin\"],\"components\":[{\"componentId\":\"ohs\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assist\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ccc\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"tie\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ude\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"VCC\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assistops\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"wfm\",\"componentClientId\":null,\"componentAccountId\":null}]}]}]},{\"clientId\":\"testclientone\",\"clientDisplayName\":\"Test Client One\",\"accounts\":[{\"accountId\":\"testclientone\",\"accountDisplayName\":\"Test Client One\",\"packageCode\":\"custom\",\"products\":[{\"productId\":\"chat\",\"roles\":[\"developer\",\"admin\"],\"components\":[{\"componentId\":\"ohs\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assist\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ccc\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"tie\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"ude\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"VCC\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"assistops\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"wfm\",\"componentClientId\":null,\"componentAccountId\":null},{\"componentId\":\"messaging\",\"componentClientId\":\"testclientone\",\"componentAccountId\":\"testclientone\"}]}]}]}]}";
    private MockUtils() {
        // hiding implicit public constructor
    }

    public static String getUMSRoles() {
        return UMS_ROLES_RESPONSE2;
    }
}