var map;
var map2;
var numOptionalStops = 0;
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
	if(numOptionalStops > 0){
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
			legsInformation[legIndex][stepIndex] = new StepInformation(step.duration.value, step.distance.value);
		}
	}
}

function putResultsOntoTheTable(directionResult){
	var calculatedRoute = directionResult.routes[0];
	var newHtml = "";
	var legIndex = 0;
	var stepIndex = 0;
	var originalTimeEstimate;
	var originalSpeedEstimate;
	
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
		newHtml += "<td>Time<br/>(Google's estimate)</td>";
		newHtml += "<td>Average Speed<br/>(Google's estimate)</td>";
		newHtml += "<td>Average Speed<br/>(your estimate)</td>";
		newHtml += "<td>Time<br/>(your estimate)</td>";
		newHtml += "</tr>";
		newHtml += "</thead>";
		
		newHtml += "<tbody>";
		
		//writes the individual rows
		for(stepIndex = 0; stepIndex < calculatedRoute.legs[legIndex].steps.length; stepIndex++){
			originalTimeEstimate = legsInformation[legIndex][stepIndex].timeString;
			originalSpeedEstimate = legsInformation[legIndex][stepIndex].milesPerHour();
			newHtml += "<tr>";
			newHtml += "<td>" + calculatedRoute.legs[legIndex].steps[stepIndex].instructions + "</td>";
			newHtml += "<td>" + legsInformation[legIndex][stepIndex].distanceMiles + "</td>";
			newHtml += "<td>" + originalTimeEstimate + "</td>";
			newHtml += "<td>" + originalSpeedEstimate + "</td>";
			newHtml += "<td><input type=\"text\" id=\"leg" + legIndex + "step" + stepIndex + "avgSpeed\" value=\"" + originalSpeedEstimate + "\" "; 
			newHtml += "onchange=\"estAvgSpeedChanged(" + legIndex + "," + stepIndex + ")\" /></td>";
			newHtml += "<td><h4 id=\"leg" + legIndex + "step" + stepIndex + "avgTime\">" + originalTimeEstimate + "</h4></td>";
			newHtml += "</tr>";
		}
		
		//writes the total information
		newHtml += "<tr>";
		newHtml += "<td> TOTAL </td>";
		newHtml += "<td>" + calculatedRoute.legs[legIndex].distance.text + "</td>";
		newHtml += "<td>" + calculatedRoute.legs[legIndex].duration.text + "</td>";
		newHtml += "<td></td>";
		newHtml += "<td></td>";
		newHtml += "<td><h4 id=\"leg" + legIndex + "totalTime\">" + calculateTotalTime(legIndex) + "</h4></td>";
		newHtml += "</tr>";
		
		newHtml += "</tbody>";
		newHtml += "</table>";
		
	}
	
	document.getElementById('TablesShowingLegs').innerHTML += newHtml;
					
}

function estAvgSpeedChanged(legIndex,stepIndex){
	var newTimeFieldID='leg' + legIndex + 'step' + stepIndex + 'avgTime';
	var newSpeedFieldID = 'leg' + legIndex + 'step' + stepIndex + 'avgSpeed';
	legsInformation[legIndex][stepIndex].setNewNumSeconds(document.getElementById(newSpeedFieldID).value);
	document.getElementById(newTimeFieldID).innerHTML = legsInformation[legIndex][stepIndex].newTimeString;
	document.getElementById("leg" + legIndex + "totalTime").innerHTML = calculateTotalTime(legIndex);
}

function calculateTotalTime(legIndex){
	var totalNumSeconds = 0;
	var stepIndex = 0;
	var totalTimeMeasure;
	for(stepIndex = 0; stepIndex < legsInformation[legIndex].length; stepIndex++){
		totalNumSeconds += legsInformation[legIndex][stepIndex].newNumSeconds;
	}
	totalTimeMeasure = new TimeMeasure(totalNumSeconds);
	return totalTimeMeasure.getTimeString();
}

function StepInformation(numSeconds, numMeters){
	this.numSeconds = numSeconds;
	this.numMeters = numMeters;
	var timeMeasure = new TimeMeasure(numSeconds);
	this.timeString = timeMeasure.getTimeString();
	this.newTimeString = "";
	this.distanceMiles = (numMeters/(1609.34)).toFixed(0);
	this.newNumSeconds = numSeconds;
	this.setNewNumSeconds = function(newNumMilesPerHour){
		var newMetersPerSecond = newNumMilesPerHour*(1609.34/3600);
		this.newNumSeconds = this.numMeters/newMetersPerSecond;
		var newTimeMeasure = new TimeMeasure(this.newNumSeconds);
		this.newTimeString = newTimeMeasure.getTimeString();
	}
	this.milesPerHour = function() {
		if(numSeconds > 0){
			return ((numMeters/numSeconds)*(3600/(1609.34))).toFixed(0);
		}
		else{
			return "N/A";
		}
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
