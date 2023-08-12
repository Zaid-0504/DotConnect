package com.example.dotconnect.Model;

public class Auth{
	private String profileUrl;
	private String coverUrl;
	private String uid;
	private String userToken;
	private String name;
	private String email;

	public String getProfileUrl(){
		return profileUrl;
	}

	public String getCoverUrl(){
		return coverUrl;
	}

	public String getUid(){
		return uid;
	}

	public String getUserToken(){
		return userToken;
	}

	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
