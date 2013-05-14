package com.zrd.rtp.model.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.zrd.rtp.model.googleClient.BaseApiRequest;
import com.zrd.rtp.model.googleClient.DirectionsApiRequest;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;

import javax.ws.rs.core.UriBuilder;

public class GoogleClientTest {

	public static void main(String[] args){
		/*
		String baseUrl = "http://maps.googleapis.com";
		String[] pathValues = {"maps","api","distancematrix","json"};
		HashMap<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("origins", "Seattle|Victoria, BC");
		queryParams.put("destinations", "San Francisco|Vancouver, BC");
		queryParams.put("sensor", "false");
		
		BaseApiRequest request = new BaseApiRequest(baseUrl,pathValues,queryParams);
		System.out.println(request.execute());
		
		
		String[] origins = {"Seattle","Vancouver BC"};
		String[] destinations = {"San Francisco","Vancouver BC"};
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(origins, destinations);
		System.out.println(request.execute());
		*/
		String origin = "Madison, WI";
		String destination = "Los Angeles, CA";
		String[] waypoints = {"Davenport, IA","Kansas City, MO"};
		DirectionsApiRequest request = DirectionsApiRequest.makeRequest(origin, destination, waypoints);
		System.out.println(request.execute());
	}
}