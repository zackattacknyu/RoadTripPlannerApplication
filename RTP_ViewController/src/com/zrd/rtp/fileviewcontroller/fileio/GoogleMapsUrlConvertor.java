package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;


public class GoogleMapsUrlConvertor {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
		
		Scanner sc = new Scanner(System.in);
		String url = sc.nextLine();
		
		String[] addrs = getAddressesFromURL(url);
		
		for(String addr: addrs){
			System.out.println("|" + addr + "|");
		}
		
		System.out.println(getUrlFromAddresses(addrs));
		

	}
	
	/**
	 * This takes in a string array of addresses and returns the google maps URL
	 * @param addresses
	 * @return the URL that can be used to call Google Maps
	 * @throws UnsupportedEncodingException 
	 */
	public static String getUrlFromAddresses(String[] addresses) throws UnsupportedEncodingException{
		StringBuilder googleURL = new StringBuilder();
		
		//assembles the base and the saddr string
		googleURL.append("https://maps.google.com/maps?saddr=");
		googleURL.append(URLEncoder.encode(addresses[0],"UTF-8"));
		
		//assembles the daddr string
		googleURL.append("&daddr=");
		googleURL.append(URLEncoder.encode(addresses[1],"UTF-8"));
		for(int index = 2; index < addresses.length; index++){
			googleURL.append(URLEncoder.encode(" to:","UTF-8"));
			googleURL.append(URLEncoder.encode(addresses[index], "UTF-8"));
		}
		
		return googleURL.toString();
	}
	
	/**
	 * This takes in a google maps URL and returns the addresses that were typed
	 * @param url		the google maps URL	
	 * @return		the String array of addresses that were typed in
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 */
	public static String[] getAddressesFromURL(String url) throws URISyntaxException, UnsupportedEncodingException{
		
		String[] parts = url.split("\\x3F|\\x26"); //ampersand or question mark

		String baseURL = parts[0];
		ArrayList<URI> paramURIs = new ArrayList<URI>(parts.length);
		ArrayList<String> urlParams = new ArrayList<String>(parts.length);
		String startAddr = "";
		String destAddr = "";
		ArrayList<String> waypoints = new ArrayList<String>(parts.length);
		String[] dAddrParts;
		String[] sAddrParts;
		String[] outputAddrs;
		
		//gets the URLs for each of the query parameters
		for(String paramPart: parts){
			paramURIs.add(new URI(baseURL + "?" + paramPart));
		}
		
		//decodes the individual URLs found above
		for(URI param: paramURIs){
			urlParams.add(URLDecoder.decode(param.getQuery(),"UTF-8"));
		}
		
		//reads the query parts found above
		for(String urlPart: urlParams){
			if(urlPart.startsWith("daddr=")){
				dAddrParts = urlPart.split("=|to:");
				
				//the last part is the destination address
				destAddr = dAddrParts[dAddrParts.length - 1];
				
				//gets the waypoints between start and end
				for(int index = 1; index < dAddrParts.length - 1; index++){
					waypoints.add(dAddrParts[index].trim());
				}
			}else if(urlPart.startsWith("saddr=")){
				
				//gets the starting address
				sAddrParts = urlPart.split("=");
				startAddr = sAddrParts[1].trim();
			}
		}
		
		//puts everything found into an output string array
		outputAddrs = new String[waypoints.size() + 2];
		outputAddrs[0] = startAddr;
		int index = 1;
		for(String waypoint: waypoints){
			outputAddrs[index++] = waypoint;
		}
		outputAddrs[outputAddrs.length - 1] = destAddr;

		
		return outputAddrs;

	}
}
