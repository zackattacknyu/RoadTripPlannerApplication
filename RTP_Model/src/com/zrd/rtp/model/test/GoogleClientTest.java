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
	
		String[] origins = {"Seattle","Vancouver BC"};
		String[] destinations = {"San Francisco","Vancouver BC"};
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(origins, destinations);
		System.out.println(request.execute());
		/*
		String origin = "Madison, WI";
		String destination = "Los Angeles, CA";
		String[] waypoints = {"Davenport, IA","Kansas City, MO"};
		DirectionsApiRequest request = DirectionsApiRequest.makeRequest(origin, destination, waypoints);
		System.out.println(request.execute());*/
	}
}
