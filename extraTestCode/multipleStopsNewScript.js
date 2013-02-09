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
	makeMapContainer(0);
	document.getElementById("sidebarTitle0").innerHTML=
        	"Route with " + makeRouteHeader(0);
  	for(x = 1; x < binaryStrings.length; x++){
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
  	document.getElementById('routeOptionsInfo').innerHTML += getBodyTableHtml(1,binaryStrings.length);
}

function getBodyTableHtml(startingRowNumber, endingRowNumber){
	var tableRowHtml = "<tbody>";
  	for(x = startingRowNumber; x < endingRowNumber; x++){
  		tableRowHtml += "<tr align=\"center\">";
  		tableRowHtml += "<td id=\"route" + x + "InfoHeader\">";
		tableRowHtml += "</td>";
		tableRowHtml += "<td id=\"route" + x + "InfoTime\">";
		tableRowHtml += "</td>";
		tableRowHtml += "<td id=\"route" + x + "InfoDistance\">";
		tableRowHtml += "</td>";
		tableRowHtml += "</tr>";
  	}
  	tableRowHtml += "</tbody>";
  	return tableRowHtml;
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
        if(numOptionalStops < 9){
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
	document.getElementById("OptionsShown").innerHTML += getMapContainerHtml(mapNumber);
}

function getMapContainerHtml(mapNumber){
	var newHtml = "<div id=\"mapDirectionsContainer" + mapNumber + "\" style=\"height:50%; width:100%;float:left\">";
	newHtml += 	"<h3 id=\"sidebarTitle" + mapNumber + "\" style=\"margin: 0; padding: 0\"></h3>";
	newHtml += "<div id=\"sidebar" + mapNumber + "\"";
	newHtml += "style=\"float: left; position: relative; width: 33%; height:50%; padding: 0; margin: 0\">";
	newHtml += "</div>";
	newHtml += "<div id=\"mapContainer" + mapNumber+ "\"";
	newHtml += "style=\"float: left; position: relative; width: 67%; height: 700px\">";
	newHtml += "</div>";
	newHtml += "</div>";
	return newHtml;
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
		
		//makes the information headers
  		document.getElementById('routeOptionsInfo').innerHTML = getInitialTableHtml();
  		document.getElementById('routeOptionsInfo').innerHTML += getBodyTableHtml(1,binaryStrings.length);
  		
  		var stops = [];
		var binaryStringForNumber;
		var stopNumber = 0;
		var stopIndex = 0;
		for(mapNumber = 0; mapNumber < binaryStrings.length; mapNumber++){
			binaryStringForNumber = binaryStrings[mapNumber];
			
			for(stopIndex = 0; stopIndex < numOptionalStops; stopIndex++){
				stopNumber = stopIndex+1;
				currentStop = document.getElementById("tripStop" + stopNumber);
				if(binaryStringForNumber.charAt(stopIndex) == '1'){
					document.getElementById("route" + mapNumber + "InfoHeader").innerHTML+=
	        			stopNumber + "-";
				}
			}
		}
		
	  		
		// initializeAllMaps(); 
		// var optionIndex;
        // var start = document.getElementById("tripStart").value;
        // var end = document.getElementById("tripEnd").value;
//         
        // //writes the base directions, no stops
        // var request = {
		// origin:start,
		// destination:end,
		// travelMode: google.maps.TravelMode.DRIVING
		// }
		// directionsService.route(request, function(result, status) {
	        // if (status == google.maps.DirectionsStatus.OK) {
	          // directionsDisplayAll[0].setDirections(result);
	          // renderInitialDirectionResult(result);
	        // }
	    // });
// 	    
	    // //writes the rest of the directions
        // for(optionIndex = 1; optionIndex < binaryStrings.length; optionIndex++){
        	// getDirections(optionIndex,start,end);
        // }
          
                   
}
function renderNewRouteResults(directionResult, mapNumber){
        var calculatedRoute = new DirectionResultInfo(directionResult);
        newRouteTime = calculatedRoute.getRouteTime();
        newRouteDistance = calculatedRoute.getRouteDistance(); 
        
        var newRouteDistDiff = new DistanceMeasure(newRouteDistance - baseRouteDistance);
        var newRouteTimeDiff = new TimeMeasure(newRouteTime - baseRouteTime);
        
        //writes the results to the HTML element
        document.getElementById("sidebarTitle" + mapNumber).innerHTML=
        	"Route with " + makeRouteHeader(mapNumber);
        
        document.getElementById("route" + mapNumber + "InfoHeader").innerHTML =
        	makeRouteHeader(mapNumber);
        
        document.getElementById("route" + mapNumber + "InfoDistance").innerHTML =
        	newRouteDistDiff.getMilesString();
        
        document.getElementById("route" + mapNumber + "InfoTime").innerHTML = 
        		newRouteTimeDiff.getTimeString();
        
        
}
function renderInitialDirectionResult(directionResult){
        var calculatedRoute = new DirectionResultInfo(directionResult);
        baseRouteDistance = calculatedRoute.getRouteDistance();
        baseRouteTime = calculatedRoute.getRouteTime();
        
        
}

function DirectionResultInfo(directionResult){
	this.calculatedRoute = directionResult.routes[0];
	this.getRouteDistance = function(){
		var routeDistance = 0;
		for(i = 0; i < this.calculatedRoute.legs.length; i++){
        	routeDistance += this.calculatedRoute.legs[i].distance.value;
        }
        return routeDistance; 
	}
	this.getRouteTime = function(){
		var routeTime = 0;
		for(i = 0; i < this.calculatedRoute.legs.length; i++){
        	routeTime += this.calculatedRoute.legs[i].duration.value;
        }
        return routeTime;
	}
}

function DistanceMeasure(numMeters){
	this.numMeters = numMeters;
	this.numMiles = numMeters/(1609.34);
	this.getMilesString = function() {
		return this.numMiles.toFixed(0);
	}
}

function TimeMeasure(numSeconds){
	this.numSeconds = numSeconds;
	this.numMinutes = numSeconds/60;
	this.timeHoursPart = Math.floor(this.numMinutes/60);
	this.timeMinsPart = this.numMinutes%60;
	this.getTimeString = function(){
		if(this.timeHoursPart > 0){
			return this.timeHoursPart.toFixed(0) + " hours " + this.timeMinsPart.toFixed(0) + " mins";
		}
		else{
			return this.timeMinsPart.toFixed(0) + " mins";
		}
	}
}
