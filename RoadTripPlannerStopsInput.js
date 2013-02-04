/*This set of functions is used for the stops input form. 
 * It can be used to add stops to the list of stops
 * 		and remove a stop from the existing list. 
 * It works like google directions where you can add destinations
 * 		and go to individual ones and remove them. 
 */

var nextStopNumber = 4;
$(document).ready(function () {
		
		hideFirstAndLastCheckbox();
		
		//this adds a stop to the form
        $('#addStopButton').bind('click',function() {
        	var stops = $("#stopsForm");
        	stops.append(getNewRowStopHtml(nextStopNumber));
        	
        	if(nextStopNumber == 3){
        		showRemoveButtons();
        	}
        	
			nextStopNumber++;
			
			hideFirstAndLastCheckbox();
        })
});

function getNewRowStopHtml(nextStopNumber){
	var newRowStopHtml = "<tr id=\"stop" + nextStopNumber + "Row\"><td id=\"stop" + nextStopNumber + "Label\">" + nextStopNumber +"</td>";
	newRowStopHtml += "<td><input type=\"text\" id=\"stop" + nextStopNumber +"Input\" size=\"75\"/></td>";
	newRowStopHtml += "<td><input type=\"button\" id=\"stop" + nextStopNumber + "RemoveButton\" value=\"Remove Stop\" onclick=\"removeStop(" + nextStopNumber + ")\"/></td>";
	newRowStopHtml += "<td id=\"stop" + nextStopNumber + "RequiredText\">Required?</td>";
	newRowStopHtml += "<td><input type=\"checkbox\" id=\"stop" + nextStopNumber + "RequiredCheckbox\"/></td>"
	newRowStopHtml += "</tr>";
	return newRowStopHtml;
}

/*
 * This removes a stop from the list. When you remove a stop
 * 		from the list, the ones after it get moved down a number. 
 * This script accomodates that. Since all the attributes are changing
 * 		when you move the stops, including the IDs, the stops are put
 * 		into arrays first and then the information on them is changed. 
 * If you try to change it on the fly, there will be naming conflicts
 * 		since we are changing the IDs. 
 */
function removeStop(stopNumber){
	var newStopIndex;
	
	//removes the stop
	$("#stop" + stopNumber + "Row").remove();
	
	//initializing arrays for transfer of info on remaining stops
	var rowsToChange = new Array();
	var labelsToChange = new Array();
	var inputsToChange = new Array();
	var removeButtonsToChange = new Array();
	var requiredTextsToChange = new Array();
	var requiredCheckboxesToChange = new Array();
	var arrayIndex = 0;
	
	//puts the existing stops into a series of arrays
	for(var stopIndex = stopNumber + 1; stopIndex < nextStopNumber; stopIndex++){
		rowsToChange.push($("#stop" + stopIndex + "Row"));
		labelsToChange.push($("#stop" + stopIndex + "Label"));
		inputsToChange.push($("#stop" + stopIndex + "Input"));
		removeButtonsToChange.push($("#stop" + stopIndex + "RemoveButton"));
		requiredTextsToChange.push($("#stop" + stopIndex + "RequiredText"));
		requiredCheckboxesToChange.push($("#stop" + stopIndex + "RequiredCheckbox"));
	}
	
	//it then uses the arrays to change the info for the remaining stops. 
	for(var stopIndex = stopNumber + 1; stopIndex < nextStopNumber; stopIndex++){
		newStopIndex = stopIndex - 1;	
		rowsToChange[arrayIndex].attr('id',"stop" + newStopIndex + "Row");
		labelsToChange[arrayIndex].html(newStopIndex);
		labelsToChange[arrayIndex].attr('id',"stop" + newStopIndex + "Label");
		inputsToChange[arrayIndex].attr('id',"stop" + newStopIndex + "Input");
		removeButtonsToChange[arrayIndex].attr('id',"stop" + newStopIndex + "RemoveButton");
		removeButtonsToChange[arrayIndex].attr('onclick',"removeStop(" + newStopIndex + ")")
		requiredTextsToChange[arrayIndex].attr('id',"stop" + newStopIndex + "RequiredText");
		requiredCheckboxesToChange[arrayIndex].attr('id',"stop" + newStopIndex + "RequiredCheckbox");
		
		arrayIndex++;
	}
	
	nextStopNumber--;
	
	if(nextStopNumber == 3){
		hideRemoveButtons();
	}
	
	hideFirstAndLastCheckbox();
}

//makes sure stop 1 and 2 have remove button hidden if they are the only ones left
function hideRemoveButtons(){
	$("#stop1RemoveButton").hide();
	$("#stop2RemoveButton").hide();		
}

//makes sure stop 1 and 2 have remove button shown after they are not the only ones left
function showRemoveButtons(){
	$("#stop1RemoveButton").show();
	$("#stop2RemoveButton").show();		
}

//hides the required checkbox for the first and last stop, since those are the start and end
function hideFirstAndLastCheckbox(){
	var lastStopNumber = nextStopNumber - 1;
	$("#stop1RequiredText").hide();
	$("#stop1RequiredCheckbox").hide();
	$("#stop" + lastStopNumber + "RequiredText").hide();
	$("#stop" + lastStopNumber + "RequiredCheckbox").hide();
	
	//makes sure the required checkbox shows for the rest of the stops
	if(lastStopNumber > 2){
		for(var stopNumber = 2; stopNumber < lastStopNumber; stopNumber++){
			$("#stop" + stopNumber + "RequiredText").show();
			$("#stop" + stopNumber + "RequiredCheckbox").show();
		}
	}
	
}

