package com.example.dotconnect.Model;

import java.util.List;

public class SearchResponse{
	private List<SearchResultItem> result;
	private String message;
	private int status;

	public void setResult(List<SearchResultItem> result){
		this.result = result;
	}

	public List<SearchResultItem> getResult(){
		return result;
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