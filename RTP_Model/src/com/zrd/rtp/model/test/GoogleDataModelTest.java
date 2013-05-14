package com.zrd.rtp.model.test;

import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleDataModel.DistanceMatrixData;
import com.zrd.rtp.model.googleDataModel.DistanceMatrixElement;

public class GoogleDataModelTest {

	public static void main(String[] args){
		
		
		String[] origins = {"Seattle, WA","Boston, MA"};
		String[] destinations = {"San Diego, CA","Miami, FL"};
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(origins, destinations);
		
		DistanceMatrixData returnData = DistanceMatrixData.getDataFromJson(request.execute());
		
		for(DistanceMatrixElement[] currentRow: returnData.getMatrix()){
			for(DistanceMatrixElement currentEntry: currentRow){
				System.out.println(currentEntry.getDistance().getText());
				System.out.println(currentEntry.getDistance().getValue());
				System.out.println();
				System.out.println(currentEntry.getDuration().getText());
				System.out.println(currentEntry.getDuration().getValue());
				System.out.println();
				System.out.println();
			}
		}
		
	}
}
