package com.tfsc.ilabs.selfservice.security.service;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class UserSession {

    private String userId;
    private String userName;
    private String accessToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
