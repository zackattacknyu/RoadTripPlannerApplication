var map;
var map2;
var numOptionalStops = 1;
var optionalStops = new Array();
var directionsService = new google.maps.DirectionsService();
var directionsDisplay;
var route1Distance;
var route1Time;
var route2Distance;
var route2Time;
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
  	for(x = 1; x < binaryStrings.length; x++){
  		document.getElementById('routeOptionsInfo').innerHTML += 
  			"<h3 id=\"route" + x + "InfoDistance\">Added Distance with " + makeRouteHeader(x) +": </h3>";
		
		document.getElementById('routeOptionsInfo').innerHTML += 
  			"<h3 id=\"route" + x + "InfoTime\">Added Time with " + makeRouteHeader(x) +": </h3>";
  	}
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
        // Configures how the map is created
        //directionsDisplay = new google.maps.DirectionsRenderer();
        //directionsDisplay2 = new google.maps.DirectionsRenderer();
        mapOptions = {
                center: new google.maps.LatLng(39.8106, -97.0569),
                zoom: 4,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                panControl: true
        };

        // Creates and displays the map
        //map = new google.maps.Map(document.getElementById("mapContainer0"), mapOptions);
        //map2 = new google.maps.Map(document.getElementById("mapContainer1"), mapOptions);
        //directionsDisplay.setMap(map);
        //directionsDisplay2.setMap(map2);
        //directionsDisplay.setPanel(document.getElementById("sidebar"));
        //directionsDisplay2.setPanel(document.getElementById("sidebar2"));
}

//this adds an optional stop to the itinerary
function addOptionalStop(){
		//google has a limit of 8 waypoints to be used by their API
        if(numOptionalStops < 8){
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
	var headerText = "waypoints of ";
	var binaryStringForNumber = binaryStrings[mapNumber];
	var stopInputTextField;
	var stopNumber;
	if(mapNumber == 0){
		return "no added waypoints";
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
	console.log("mapNumber = " + mapNumber);
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
          //renderDirectionResults(result);
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
        for(optionIndex = 0; optionIndex < binaryStrings.length; optionIndex++){
        	getDirections(optionIndex,start,end);
        }
          /*
          var stops = [];
          var stop1 = document.getElementById("tripStop1");
          stops.push({
          	location:stop1.value,
          	stopover:true
          });
        var request = {
            origin:start,
            destination:end,
            travelMode: google.maps.TravelMode.DRIVING
         };
         
         var request2 = {
            origin:start,
            destination:end,
            waypoints: stops,
            travelMode: google.maps.TravelMode.DRIVING
         };
          directionsService.route(request, function(result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
              directionsDisplay.setDirections(result);
              renderDirectionResults(result);
            }
          });
          
          directionsService.route(request2, function(result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
              directionsDisplay2.setDirections(result);
              renderDirectionResults2(result);
            }
          });
		*/
          
                   
}
function renderDirectionResults2(directionResult){
        var calculatedRoute = directionResult.routes[0];
       	route2Distance = 0; route2Time = 0;
        for(i = 0; i < calculatedRoute.legs.length; i++){
        	route2Distance += calculatedRoute.legs[i].distance.value;
        	route2Time += calculatedRoute.legs[i].duration.value;
        }
        
        var route2DistDiff = (route2Distance - route1Distance)/(1609.34);
        var route2TimeDiff = (route2Time - route1Time)/60;
        
        var route2TimeDiffHoursPart = Math.floor(route2TimeDiff/60);
        var route2TimeDiffMinsPart = route2TimeDiff%60;
        /*
        document.getElementById("routeInfoDistance").innerHTML =
        	routeInfoDistTitle + route2DistDiff.toFixed(0) + " mi";
        
        if(route2TimeDiffHoursPart > 0){
        	document.getElementById("routeInfoTime").innerHTML = 
        		routeInfoTimeTitle + route2TimeDiffHoursPart.toFixed(0) + " hours " + 
        		route2TimeDiffMinsPart.toFixed(0) + " min";	
        }
        else{
        	document.getElementById("routeInfoTime").innerHTML = 
        		routeInfoTimeTitle + route2TimeDiff.toFixed(0) + " mins";
        }*/
        
        
}
function renderDirectionResults(directionResult){
        var calculatedRoute = directionResult.routes[0].legs[0];
        route1Distance = calculatedRoute.distance.value;
        route1Time = calculatedRoute.duration.value;
        
        
}