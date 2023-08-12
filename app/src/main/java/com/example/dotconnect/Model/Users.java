package com.example.dotconnect.Model;

public class Users {

    private String profileUrl;
    private String coverUrl;
    private String uid;
    private String userToken;
    private String name;
    private String email;

    public Users( String uid, String name, String email,String profileUrl, String coverUrl,String userToken) {
        this.profileUrl = profileUrl;
        this.coverUrl = coverUrl;
        this.uid = uid;
        this.userToken = userToken;
        this.name = name;
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
