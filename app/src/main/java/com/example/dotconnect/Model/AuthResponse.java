package com.example.dotconnect.Model;

public class AuthResponse {
	private Auth auth;
	private String message;
	private int status;

	public Auth getAuth(){
		return auth;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
