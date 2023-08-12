package com.example.dotconnect.Model;

public class ProfileResponse{
	private Profile profile;
	private String state;
	private String message;
	private int status;

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	public Profile getProfile(){
		return profile;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}
