var map;
var map2;
var numOptionalStops = 1;
var optionalStops = new Array();
var directionsService = new google.maps.DirectionsService();
var directionsDisplay;
var baseRouteDistance;
var baseRouteTime;
var newRouteDistance;
var newRouteTime;
var routeInfoDistTitle = "Added Distance with Optional Stop: ";
var routeInfoTimeTitle = "Added Time with Optional Stop: ";


var binaryStrings = new Array();
var mapOptions;
var directionsDisplayAll = new Array();
var allMaps = new Array();
var numBinaryStrings;

function makeBinaryStrings(){
	numBinaryStrings = Math.pow(2,numOptionalStops);
	
	for(x = 0; x < numBinaryStrings; x++){
		binaryStrings[x] = AddLeadingZeros(x.toString(2),numOptionalStops);
	}
}

function initializeAllMaps () {
	var tableRowHtml;
	
	document.getElementById("OptionsShown").innerHTML = "";
  	for(x = 0; x < binaryStrings.length; x++){
  		makeMapContainer(x);
  	}
  	
  	for(x = 0 ; x < binaryStrings.length; x++){
  		directionsDisplayAll[x] = new google.maps.DirectionsRenderer();
  		allMaps[x] = new google.maps.Map(document.getElementById("mapContainer" + x),mapOptions);
  		directionsDisplayAll[x].setMap(allMaps[x]);
        directionsDisplayAll[x].setPanel(document.getElementById("sidebar" + x));
  	}
  	
  	//makes the information headers
  	document.getElementById('routeOptionsInfo').innerHTML = getInitialTableHtml();
  	tableRowHtml = "<tbody>";
  	for(x = 1; x < binaryStrings.length; x++){
  		tableRowHtml += "<tr align=\"center\">";
  		tableRowHtml += "<td>";
  		tableRowHtml += makeRouteHeader(x);
		tableRowHtml += "</td>";
		tableRowHtml += "<td id=\"route" + x + "InfoTime\">";
		tableRowHtml += "</td>";
		tableRowHtml += "<td id=\"route" + x + "InfoDistance\">";
		tableRowHtml += "</td>";
		tableRowHtml += "</tr>";
  	}
  	tableRowHtml += "</tbody>";
  	document.getElementById('routeOptionsInfo').innerHTML += tableRowHtml;
}

function getInitialTableHtml(){
	var headerHtml = "";
	headerHtml += "<thead>";
	headerHtml += "<tr>";
	headerHtml += "<th>";
	headerHtml += "Waypoints";
	headerHtml += "</th>";
	headerHtml += "<th>";
	headerHtml += "Added Time";
	headerHtml += "</th>";
	headerHtml += "<th>";
	headerHtml += "Added Distance (miles)";
	headerHtml += "</th>"
	headerHtml += "</tr>";
	headerHtml += "</thead";
	return headerHtml;
}

function AddLeadingZeros(startingString, lengthRequired){
	var returnString = startingString;
	
	while(returnString.length < lengthRequired){
		returnString = "0" + returnString;
	}

	return returnString;
}

//this initializes the map
function initialize(){
        mapOptions = {
                center: new google.maps.LatLng(39.8106, -97.0569),
                zoom: 4,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                panControl: true
        };
}

//this adds an optional stop to the itinerary
function addOptionalStop(){
		
		/*
		 * Google has a query limit of 10 queries at a time, 
		 * 		so the number of optional stops must be limited
		 * 		to three. 
		 */
        if(numOptionalStops < 2){
        	numOptionalStops++;
            var currentInnerHTML=document.getElementById("StopsForm").innerHTML;
            var newInnerHTML=currentInnerHTML += "<div id=\"Stop" + numOptionalStops + "TextLabel\">" + 
                    "Optional Stop " + numOptionalStops + ": <input type=\"text\" id=\"tripStop" + 
                    numOptionalStops + "\"><br>" + "</div>";
            document.getElementById("StopsForm").innerHTML=newInnerHTML;	
        }
}

//this removes a stop from the itinerary
function removeOptionalStop(){
	if(numOptionalStops > 1){
		var stopElement = document.getElementById("Stop" + numOptionalStops + "TextLabel");
		stopElement.parentNode.removeChild(stopElement);		
		numOptionalStops--;
		
	}
}

//this makes an individual direction and map container
function makeMapContainer(mapNumber){
	var newHtml = "<div id=\"mapDirectionsContainer" + mapNumber + "\" style=\"height:50%; width:100%;float:left\">";
	newHtml += 	"<h3 id=\"sidebarTitle" + mapNumber + "\" style=\"margin: 0; padding: 0\">Route with " + makeRouteHeader(mapNumber) + "</h3>";
	newHtml += "<div id=\"sidebar" + mapNumber + "\"";
	newHtml += "style=\"float: left; position: relative; width: 33%; height:50%; padding: 0; margin: 0\">";
	newHtml += "</div>";
	newHtml += "<div id=\"mapContainer" + mapNumber+ "\"";
	newHtml += "style=\"float: left; position: relative; width: 67%; height: 700px\">";
	newHtml += "</div>";
	newHtml += "</div>";
	
	document.getElementById("OptionsShown").innerHTML += newHtml;
}

//this makes the route header text for the map
function makeRouteHeader(mapNumber){
	var headerText = "";
	var binaryStringForNumber = binaryStrings[mapNumber];
	var stopInputTextField;
	var stopNumber;
	if(mapNumber == 0){
		return "No Added Waypoints";
	}
	else{
		for(charIndex = 0; charIndex < binaryStringForNumber.length; charIndex++){
			if(binaryStringForNumber.charAt(charIndex) == '1'){
				stopNumber = charIndex + 1;
				stopInputTextField = document.getElementById("tripStop" + stopNumber);
				headerText += stopInputTextField.value;
				headerText += " ; ";
			}
		}
	}
	
	//removes the last semicolon before returning header
	return headerText.substring(0,headerText.length-2);
}

function getDirections(mapNumber, start, end){
	var stops = [];
	var binaryStringForNumber = binaryStrings[mapNumber];
	var currentStopInfo;
	var stopNumber;
	var stopIndex;
	var currentStop;
	for(stopIndex = 0; stopIndex < numOptionalStops; stopIndex++){
		stopNumber = stopIndex+1;
		currentStop = document.getElementById("tripStop" + stopNumber);
		if(binaryStringForNumber.charAt(stopIndex) == '1'){
			stops.push({
			location:currentStop.value,
			stopover:true
			})	
		}
	}
	var request = {
		origin:start,
		destination:end,
		waypoints:stops,
		travelMode: google.maps.TravelMode.DRIVING
	}
	directionsService.route(request, function(result, status) {
        if (status == google.maps.DirectionsStatus.OK) {
          directionsDisplayAll[mapNumber].setDirections(result);
          renderNewRouteResults(result, mapNumber);
        }
    });
          
}

//this renders the directions
function getTheOptions(){
		makeBinaryStrings();
		initializeAllMaps(); 
		var optionIndex;
        var start = document.getElementById("tripStart").value;
        var end = document.getElementById("tripEnd").value;
        
        //writes the base directions, no stops
        var request = {
		origin:start,
		destination:end,
		travelMode: google.maps.TravelMode.DRIVING
		}
		directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
	          directionsDisplayAll[0].setDirections(result);
	          renderInitialDirectionResult(result);
	        }
	    });
	    
	    //writes the rest of the directions
        for(optionIndex = 1; optionIndex < binaryStrings.length; optionIndex++){
        	getDirections(optionIndex,start,end);
        }
          
                   
}
function renderNewRouteResults(directionResult, mapNumber){
        var calculatedRoute = directionResult.routes[0];
        
        /*
         * gets the new route distance, in meters
         * gets the new route time, in seconds
         */
       	newRouteDistance = 0; newRouteTime = 0;
        for(i = 0; i < calculatedRoute.legs.length; i++){
        	newRouteDistance += calculatedRoute.legs[i].distance.value;
        	newRouteTime += calculatedRoute.legs[i].duration.value;
        }
        
        //gets the difference and converts it to miles
        var newRouteDistDiff = (newRouteDistance - baseRouteDistance)/(1609.34);
        
        //gets the difference and converts it to x hours y mins format. 
        var newRouteTimeDiff = (newRouteTime - baseRouteTime)/60;
        var newRouteTimeDiffHoursPart = Math.floor(newRouteTimeDiff/60);
        var newRouteTimeDiffMinsPart = newRouteTimeDiff%60;
        
        //writes the results to the HTML element
        document.getElementById("route" + mapNumber + "InfoDistance").innerHTML =
        	newRouteDistDiff.toFixed(0);
        
        if(newRouteTimeDiffHoursPart > 0){
        	document.getElementById("route" + mapNumber + "InfoTime").innerHTML = 
        		newRouteTimeDiffHoursPart.toFixed(0) + " hours " + 
        		newRouteTimeDiffMinsPart.toFixed(0) + " min";	
        }
        else{
        	document.getElementById("route" + mapNumber + "InfoTime").innerHTML = 
        		newRouteTimeDiff.toFixed(0) + " mins";
        }
        
        
}
function renderInitialDirectionResult(directionResult){
        var calculatedRoute = directionResult.routes[0].legs[0];
        baseRouteDistance = calculatedRoute.distance.value;
        baseRouteTime = calculatedRoute.duration.value;
        
        
}