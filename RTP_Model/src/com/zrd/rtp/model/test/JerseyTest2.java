package com.zrd.rtp.model.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import javax.ws.rs.core.UriBuilder;

public class JerseyTest2 {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException{
		
		HashSet<String> cities = new HashSet<String>();
		BufferedReader file = new BufferedReader(new FileReader(new File("C:/dev/test/facList2.txt")));
		Scanner sc = new Scanner(file);
		while(sc.hasNext()){
			cities.add(sc.nextLine());
		}
		for(String city:cities){
			getDestInfo(city + ",WI");
		}
	}
	
	public static void getDestInfo(String destination) throws InterruptedException{
		Thread.sleep(2000);
		String baseUrl = "http://maps.googleapis.com";
		String[] pathValues = {"maps","api","directions","json"};
		Hashtable<String,String> queryParams = new Hashtable<String,String>();
		queryParams.put("origin", "Madison, WI");
		queryParams.put("destination", destination);
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
		//System.out.println(webResource.toString());
		
		String output = webResourceBuilder.get(String.class);
		
		if(output.length() < 156){
			System.out.println("Manually check " + destination);
		}else{
			int legsIndex = output.indexOf("legs");
			int distanceIndex = output.indexOf("}",legsIndex);
			int durationIndex = output.indexOf("}",distanceIndex+1);
			if(legsIndex > -1 && durationIndex > 0){
				String[] info = output.substring(legsIndex,durationIndex).split("\\s");
				if(info.length > 155){
					System.out.println(destination + "\t" + info[76] + "\t " + info[155]);
				}else{
					System.out.println("Manually check " + destination);
				}
				
			}else{
				System.out.println("Manually check " + destination);
			}
			
		}
		
	}
}
