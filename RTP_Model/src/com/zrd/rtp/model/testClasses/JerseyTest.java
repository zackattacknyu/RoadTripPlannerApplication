package com.zrd.rtp.model.testClasses;

import java.util.Hashtable;
import java.util.Iterator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import javax.ws.rs.core.UriBuilder;

public class JerseyTest {

	public static void main(String[] args){
		
		String baseUrl = "http://maps.googleapis.com";
		String[] pathValues = {"maps","api","distancematrix","json"};
		Hashtable<String,String> queryParams = new Hashtable<String,String>();
		queryParams.put("origins", "Seattle|Victoria, BC");
		queryParams.put("destinations", "San Francisco|Vancouver, BC");
		queryParams.put("sensor", "false");
		Client client = Client.create();
		WebResource webResource = client.resource(baseUrl);
		WebResource.Builder webResourceBuilder;
		UriBuilder theBuilder = UriBuilder.fromPath(baseUrl);
		
		for(String path: pathValues){
			theBuilder = theBuilder.path(path);
		}
		
		webResource = client.resource(theBuilder.build(pathValues));
		
		Iterator<String> queryKeys = queryParams.keySet().iterator();
		String currentKey;
		while(queryKeys.hasNext()){
			currentKey = queryKeys.next();
			webResource = webResource.queryParam(currentKey, queryParams.get(currentKey));
		}
		
		webResourceBuilder = webResource.getRequestBuilder();
		System.out.println(webResource.toString());
		System.out.println(webResourceBuilder.get(String.class));
		
	}
}
