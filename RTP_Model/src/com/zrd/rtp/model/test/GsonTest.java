package com.zrd.rtp.model.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class GsonTest {

	public static void main(String[] args) throws JAXBException, MalformedURLException{
		
		String baseUrl = "http://maps.googleapis.com";
		String[] pathValues = {"maps","api","distancematrix","json"};
		Hashtable<String,String> queryParams = new Hashtable<String,String>();
		queryParams.put("origins", "Seattle|Victoria, BC");
		queryParams.put("destinations", "San Francisco|Vancouver, BC");
		queryParams.put("sensor", "false");
		Client client = Client.create();
		//WebResource webResource = client.resource(baseUrl);
		WebResource.Builder webResourceBuilder;
		UriBuilder theBuilder = UriBuilder.fromPath(baseUrl);
		
		for(String path: pathValues){
			theBuilder = theBuilder.path(path);
		}
		
		WebResource webResource = client.resource(theBuilder.build(pathValues));
		
		Iterator<String> queryKeys = queryParams.keySet().iterator();
		String currentKey;
		while(queryKeys.hasNext()){
			currentKey = queryKeys.next();
			webResource = webResource.queryParam(currentKey, queryParams.get(currentKey));
		}
		
		webResourceBuilder = webResource.getRequestBuilder();
		
		String output = webResourceBuilder.get(String.class);
		
		JsonParser newParser = new JsonParser();
		JsonObject resultTree = (JsonObject) newParser.parse(output);
		
		System.out.println(resultTree.get("origin_addresses"));
		System.out.println(resultTree.get("destination_addresses"));
		System.out.println(resultTree.get("status").getAsString().equals("OK"));
		//System.out.println(resultTree.get("rows"));
		JsonArray theRows = resultTree.get("rows").getAsJsonArray();
		for(JsonElement row: theRows){
			JsonArray currentRow = row.getAsJsonObject().get("elements").getAsJsonArray();
			for(JsonElement element: currentRow){
				System.out.println(element.getAsJsonObject().get("status"));
				System.out.println(element.getAsJsonObject().get("duration"));
				System.out.println(element.getAsJsonObject().get("distance"));
			}
			System.out.println();
		}
		
		
	}

}
