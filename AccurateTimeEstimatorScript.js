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

var mapOptions;
var legsInformation;

function initializeAllMaps () {
	var tableRowHtml;
	
	document.getElementById("OptionsShown").innerHTML = "";
  	makeMapContainer();
  	directionsDisplay = new google.maps.DirectionsRenderer();
  	map = new google.maps.Map(document.getElementById("mapContainer"),mapOptions);
	directionsDisplay.setMap(map);
    directionsDisplay.setPanel(document.getElementById("sidebar"));
  	
  	//makes the information headers
  	/*
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
  	*/
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
function addStop(){
		var newInnerHtml = "";
		var currentInnerHtml=document.getElementById("StopsForm").innerHTML;
		
		/*
		 * Google has a limit of 8 waypoints in directions
		 */
        if(numOptionalStops < 8){
        	numOptionalStops++;
            newInnerHtml += "<tr id=\"Stop" + numOptionalStops + "TextLabel\">";
			newInnerHtml += "<td>";
			newInnerHtml += "Stop " + numOptionalStops + ":";
			newInnerHtml += "</td>"
			newInnerHtml += "<td>"
			newInnerHtml += "<input type=\"text\" id=\"tripStop" + numOptionalStops+ "\"/>"
			newInnerHtml += "</td>"
			newInnerHtml += "</tr>"
            document.getElementById("StopsForm").innerHTML= currentInnerHtml + newInnerHtml;	
        }
}

//this removes a stop from the itinerary
function removeStop(){
	if(numOptionalStops > 1){
		var stopElement = document.getElementById("Stop" + numOptionalStops + "TextLabel");
		stopElement.parentNode.removeChild(stopElement);		
		numOptionalStops--;
		
	}
}

//this makes an individual direction and map container
function makeMapContainer(){
	var newHtml = "<div id=\"mapDirectionsContainer\" style=\"height:50%; width:100%;float:left\">";
	newHtml += 	"<h3 id=\"sidebarTitle\" style=\"margin: 0; padding: 0\">Here is the Route</h3>";
	newHtml += "<div id=\"sidebar\"";
	newHtml += "style=\"float: left; position: relative; width: 33%; height:50%; padding: 0; margin: 0\">";
	newHtml += "</div>";
	newHtml += "<div id=\"mapContainer\"";
	newHtml += "style=\"float: left; position: relative; width: 67%; height: 700px\">";
	newHtml += "</div>";
	newHtml += "</div>";
	
	document.getElementById("OptionsShown").innerHTML += newHtml;
}

//this renders the directions
function getTheOptions(){
		initializeAllMaps(); 
		var optionIndex;
        var start = document.getElementById("tripStart").value;
        var end = document.getElementById("tripEnd").value;
        
        var stops = [];
		var currentStop;
		for(stopIndex = 0; stopIndex < numOptionalStops; stopIndex++){
			stopNumber = stopIndex+1;
			currentStop = document.getElementById("tripStop" + stopNumber);
			stops.push({
				location:currentStop.value,
				stopover:true
			})
		}
		var request = {
			origin:start,
			destination:end,
			waypoints:stops,
			travelMode: google.maps.TravelMode.DRIVING
		}
		directionsService.route(request, function(result, status) {
	        if (status == google.maps.DirectionsStatus.OK) {
	        	renderTable(result);
	          	directionsDisplay.setDirections(result);
	        }
	    });   
	    
	                  
}

function renderTable(directionResult){
		putResultsIntoLocalArray(directionResult);
		putResultsOntoTheTable(directionResult);
}

function putResultsIntoLocalArray(directionResult){
	var calculatedRoute = directionResult.routes[0];
	legsInformation = new Array(calculatedRoute.legs.length);
	var legIndex;
	var stepIndex;
	var step;
	
	//gets the information for each leg
	for(legIndex = 0; legIndex < calculatedRoute.legs.length; legIndex++){
		legsInformation[legIndex] = new Array(calculatedRoute.legs[legIndex].steps.length);
		
		for(stepIndex = 0; stepIndex < calculatedRoute.legs[legIndex].steps.length; stepIndex++){
			step = calculatedRoute.legs[legIndex].steps[stepIndex];
			if(step.duration > 0){
				legsInformation[legIndex][stepIndex] = new StepInformation(step.duration, step.distance);	
			}
		}
	}
}

function putResultsOntoTheTable(directionResult){
	var calculatedRoute = directionResult.routes[0];
	var newHtml = "";
	var legIndex;
	var stepIndex;
	
	for(legIndex = 0; legIndex < calculatedRoute.legs.length; legIndex++){
		//writes the table title
		newHtml += "<h3 id=\"leg" + legIndex + "TableTitle\">";
		newHtml += "Leg from " + calculatedRoute.legs[legIndex].start_address + " to " + calculatedRoute.legs[legIndex].end_address;
		newHtml += "</h3>";
		
		//starts the table
		newHtml += "<table border=\"1\" id=\"leg" + legIndex + "Table\">";
		
		//writes the table header
		newHtml += "<thead>";
		newHtml += "<tr>";
		newHtml += "<td>Direction Text</td>";
		newHtml += "<td>Distance(miles)</td>";
		newHtml += "<td>Time(Google's estimate)</td>";
		newHtml += "<td>Average Speed(Google's estimate)</td>";
		newHtml += "<td>Average Speed(your estimate)</td>";
		newHtml += "<td>Time(your estimate)</td>";
		newHtml += "</tr>";
		newHtml += "</thead>";
		
		newHtml += "<tbody>";
		
		//writes the individual rows
		for(stepIndex = 0; stepIndex < calculatedRoute.legs[legIndex].steps.length; stepIndex++){
			newHtml += "<tr>";
			newHtml += "<td>" + calculatedRoute.legs[legIndex].steps[stepIndex].html_instructions + "</td>";
			newHtml += "<td>" + calculatedRoute.legs[legIndex].steps[stepIndex].distance + "</td>";
			newHtml += "<td>" + calculatedRoute.legs[legIndex].steps[stepIndex].duration + "</td>";
			newHtml += "<td> INSERT LATER</td>";
			newHtml += "<td> INSERT LATER</td>";
			newHtml += "<td> TO BE INSERTED LATER</td>";
			/*
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].getNumMiles() + "</td>";
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].getGoogleTimeString() + "</td>";
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].getGoogleMilesPerHour() + "</td>";
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].getGoogleMilesPerHour() + "</td>";
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].getYourTimeString() + "</td>";*/
			newHtml += "</tr>";
		}
		
		newHtml += "</tbody>";
		newHtml += "</table>";
		
	}
	
	document.getElementById('TablesShowingLegs').innerHTML += newHtml;
					
}

function StepInformation(numSeconds, numMeters){
	this.numMiles = numMeters/(1609.34);
	this.numHours = numSeconds/3600;
	this.googleMilesPerHour = this.numMiles/this.numHours;
	this.yourMilesPerHour = this.googleMilesPerHour;
	this.yourNumHours = this.numMiles/this.yourMilesPerHour;
	this.getNumMiles = function(){
		return this.numMiles.toFixed(0);
	}
	this.setYourMilesPerHour = function(newSpeed){
		this.yourMilesHerHour = newSpeed;
		this.yourNumHours = this.numMiles/newSpeed;
	}
	this.getYourNewTimeString = function(){
		var newTimeMeasure = new TimeMeasure(this.yourNumHours*3600);
		return newTimeMeasure.getTimeString;
	}
	this.getGoogleTimeString = function(){
		var googleTimeMeasure = new TimeMeasure(this.NumHours*3600);
		return googleTimeMeasure.getTimeString;
	}
	this.getGoogleMilesPerHour = function(){
		return this.googleMilesPerHour.toFixed(0);
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
