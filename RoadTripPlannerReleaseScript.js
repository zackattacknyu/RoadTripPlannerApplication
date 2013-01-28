var nextStopNumber = 3;
$(document).ready(function () {
		
		hideRemoveButtons();
		hideFirstAndLastCheckbox();
		
		//this adds a stop to the form
        $('#addStopButton').bind('click',function() {
        	var stops = $("#stopsForm");
        	var newRowStopHtml = "<tr id=\"stop" + nextStopNumber + "Row\"><td id=\"stop" + nextStopNumber + "Label\">" + nextStopNumber +"</td>";
        	newRowStopHtml += "<td><input type=\"text\" id=\"stop" + nextStopNumber +"Input\"/></td>";
        	newRowStopHtml += "<td><input type=\"button\" id=\"stop" + nextStopNumber + "RemoveButton\" value=\"Remove Stop\" onclick=\"removeStop(" + nextStopNumber + ")\"/></td>";
        	newRowStopHtml += "<td id=\"stop" + nextStopNumber + "RequiredText\">Required?</td>";
        	newRowStopHtml += "<td><input type=\"checkbox\" id=\"stop" + nextStopNumber + "RequiredCheckbox\"/></td>"
        	newRowStopHtml += "</tr>";
        	stops.append(newRowStopHtml);
        	
        	//first stop added
        	if(nextStopNumber == 3){
        		$("#stop1RemoveButton").show();
				$("#stop2RemoveButton").show();	
        	}
			nextStopNumber++;
			
			hideFirstAndLastCheckbox();
        })
});

//removes a stop from the list
function removeStop(stopNumber){
	var newStopIndex;
	$("#stop" + stopNumber + "Row").remove();
	
	var rowsToChange = new Array();
	var labelsToChange = new Array();
	var inputsToChange = new Array();
	var removeButtonsToChange = new Array();
	var requiredTextsToChange = new Array();
	var requiredCheckboxesToChange = new Array();
	var arrayIndex = 0;
	
	for(var stopIndex = stopNumber + 1; stopIndex < nextStopNumber; stopIndex++){
		rowsToChange.push($("#stop" + stopIndex + "Row"));
		labelsToChange.push($("#stop" + stopIndex + "Label"));
		inputsToChange.push($("#stop" + stopIndex + "Input"));
		removeButtonsToChange.push($("#stop" + stopIndex + "RemoveButton"));
		requiredTextsToChange.push($("#stop" + stopIndex + "RequiredText"));
		requiredCheckboxesToChange.push($("#stop" + stopIndex + "RequiredCheckbox"));
	}
	
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
	hideFirstAndLastCheckbox();
}

//makes sure stop 1 and 2 have remove button hidden if they are the only ones left
function hideRemoveButtons(){
	$("#stop1RemoveButton").hide();
	$("#stop2RemoveButton").hide();		
}

//hides the required checkbox for the first and last stop, since those are the start and end
function hideFirstAndLastCheckbox(){
	var lastStopNumber = nextStopNumber - 1;
	$("#stop1RequiredText").hide();
	$("#stop1RequiredCheckbox").hide();
	$("#stop" + lastStopNumber + "RequiredText").hide();
	$("#stop" + lastStopNumber + "RequiredCheckbox").hide();
	
	if(lastStopNumber > 2){
		for(var stopNumber = 2; stopNumber < lastStopNumber; stopNumber++){
			$("#stop" + stopNumber + "RequiredText").show();
			$("#stop" + stopNumber + "RequiredCheckbox").show();
		}
	}
	
}

