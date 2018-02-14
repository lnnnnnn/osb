package edu.nuist.osbank.service;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

public class GetRequest {
	
	private String exType;
	private List<Integer> exId;
	
	
	public String getExType() {
		return exType;
	}
	public void setExType(String exType) {
		this.exType = exType;
	}
	public List<Integer> getExId() {
		return exId;
	}
	public void setExId(List<Integer> exId) {
		this.exId = exId;
	}

	public String  toString(){
		
		String str = "exType:" + exType + "; exId:" + exId;
		return str;
	}
	

	 
	
	public  static void main(String[] args){
		
		 
	}
}
