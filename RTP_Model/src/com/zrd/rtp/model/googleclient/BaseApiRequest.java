package com.zrd.rtp.model.googleClient;

import java.util.HashMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.UriBuilder;

public class BaseApiRequest {

	private String baseURL;
	private String[] pathValues;
	private HashMap<String,String> queryParameters;

	private Client client;
	private WebResource webResource;
	private WebResource.Builder webResourceBuilder;
	private UriBuilder theBuilder;
	
	protected BaseApiRequest(String baseURL, String[] pathValues, HashMap<String,String> queryParameters){
		this.baseURL = baseURL;
		this.pathValues = pathValues;
		this.queryParameters = queryParameters;
		
		initializeClient();
		initializePath();
		initializeResource();
		initializeQueryParameters();
		initializeResourceBuilder();
	}
	
	public static BaseApiRequest newApiRequest(String baseURL, String[] pathValues, HashMap<String,String> queryParameters){
		return new BaseApiRequest(baseURL,pathValues,queryParameters);
	}
	
	private void initializeClient(){
		client = Client.create();
	}
	
	private void initializePath(){
		theBuilder = UriBuilder.fromPath(baseURL);
		for(String path:pathValues){
			theBuilder = theBuilder.path(path);
		}
	}
	
	private void initializeResource(){
		webResource = client.resource(theBuilder.build(pathValues));
	}
	
	protected void initializeQueryParameters(){
		for(String key:queryParameters.keySet()){
			webResource = webResource.queryParam(key, queryParameters.get(key));
		}
	}
	
	protected void initializeResourceBuilder(){
		webResourceBuilder = webResource.getRequestBuilder();
	}
	
	public String execute(){
		System.out.println(webResource.toString());
		return webResourceBuilder.get(String.class);
	}
	
	
}
