package com.tfsc.ilabs.selfservice.common.utils;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class ThreadLocalContext {

    private ThreadLocalContext() {
        // hide implicit public constructor
    }

    public static void setThreadLocalContext(UUID sessionID, String userId) {
        setContext(sessionID, userId, UUID.randomUUID());
    }

    public static String getLocaleCode() {
        return MDC.get(Constants.LOCALE_CODE);
    }

    public static void setLocaleCode(String localeCode) {
        MDC.put(Constants.LOCALE_CODE, localeCode);
    }

    public static void clearThreadLocalContext() {
        MDC.put(Constants.SESSION_ID, "");
        MDC.put(Constants.USER_ID, "");
        MDC.put(Constants.REQUEST_ID, "");
        MDC.put(Constants.LOCALE_CODE, "");
    }

    public static void setThreadLocalContext() {
        UUID uuid = UUID.randomUUID();
        setContext(uuid, Constants.DEFAULT_USER, uuid);
    }

    private static void setMDCContext(String sessionID, String requestID, String userID) {
        MDC.put(Constants.SESSION_ID, sessionID);
        MDC.put(Constants.USER_ID, userID);
        MDC.put(Constants.REQUEST_ID, requestID);
    }

    public static void setThreadLocalContext(String sessionID, String requestID, String userID) {
        setMDCContext(sessionID, requestID, userID);
    }

    private static void setContext(UUID sessionID, String userId, UUID requestID) {
        setMDCContext(sessionID.toString(), requestID.toString(), userId);
    }

    public static void setUserAndClientContext(String userId,String clientId,String accountId){
        MDC.put("ClientID", clientId);
        MDC.put(Constants.USER_ID, userId);
        MDC.put("AccountID", accountId);
    }
}
