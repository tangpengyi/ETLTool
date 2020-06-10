package com.tpy.etlinterfacetestserver.entity;

public class User {

    private String username;
    private String userPwd;

    public User() {}

    public User(String username, String userPwd) {
        this.username = username;
        this.userPwd = userPwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
