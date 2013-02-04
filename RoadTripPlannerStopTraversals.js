var traversalStrings = new Array();
var traversalArrays = new Array();
var requiredStops = new Array();

function renderTable(){
	var numStops = $("#routeOptionsTable >tbody >tr").length;
	for(var ind = 1; ind < numStops; ind++){
		if($("#stop" + ind + "RequiredCheckbox").is(":checked")){
			requiredStops.push(ind);
		}
	}
	
	//test code
	for(var ind = 0; ind < requiredStops.length; ind++){
		console.log(requiredStops[ind]);
	}
	
	DoBfsSearchInOrder(numStops);
	var traversalsHtml = "";
	for(var ind = 1; ind < traversalArrays.length; ind++){
		traversalsHtml += "<tr><td>" + getTraversalString(traversalArrays[ind]) +"</td></tr>";
	}
	$("#Results").append(traversalsHtml);
}

function getTraversalString(traversalSequence){
	var str = "";
	for(var ind = 0; ind < traversalSequence.length; ind++){
		str += traversalSequence[ind] + " - ";
	}
	return str;
}

function DoDfsSearch(numStops){
	var stopsHash = new stopsVisitedHashMap(numStops);
	var sampletraverse = new Traversal(stopsHash,0);
	DfsTraverse(sampletraverse,numStops);
}

function DoBfsSearch(numStops){
	var stopsHash = new stopsVisitedHashMap(numStops);
	var sampletraverse = new Traversal(stopsHash,0);
	for(var numStopsToHave = 0; numStopsToHave <= numStops; numStopsToHave++){
		BfsTraverse(sampletraverse,0,numStopsToHave,numStops);
	}
}

function DoDfsSearchInOrder(numStops){
	var stopsHash = new stopsVisitedHashMap(numStops);
	var sampletraverse = new Traversal(stopsHash,0);
	DfsTraverseInOrder(sampletraverse,numStops);
}

function DoBfsSearchInOrder(numStops){
	var stopsHash = new stopsVisitedHashMap(numStops);
	var sampletraverse = new Traversal(stopsHash,0);
	for(var numStopsToHave = 0; numStopsToHave <= numStops; numStopsToHave++){
		BfsTraverseInOrder(sampletraverse,0,numStopsToHave,numStops);
	}
}

function DfsTraverse(traversal,numStops){
	traversalArrays.push(traversal.getSequence());
	var currentTraversal;
	
	for(var stopNum = 1; stopNum <= numStops; stopNum++){
		if(traversal.doesNotContainStop(stopNum)){
			currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
			currentTraversal.addStop(stopNum);
			DfsTraverse(currentTraversal,numStops);
		}
	}
}

function BfsTraverse(traversal,numStopsSoFar,numStopsAllowed,totalNumStops){
	var currentTraversal;
	
	if(numStopsSoFar == numStopsAllowed){
		traversalArrays.push(traversal.getSequence());
	}
	else{
		for(var stopNum = 1; stopNum <= totalNumStops; stopNum++){
			if(traversal.doesNotContainStop(stopNum)){
				currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
				currentTraversal.addStop(stopNum);
				BfsTraverse(currentTraversal,numStopsSoFar+1,numStopsAllowed,totalNumStops);
			}
		}
	}
}

function DfsTraverseInOrder(traversal,numStops){
	traversalArrays.push(traversal.getSequence());
	var currentTraversal;
	
	for(var stopNum = 1; stopNum <= numStops; stopNum++){
		if(traversal.doesNotContainStop(stopNum) && traversal.isStopGreaterThanLast(stopNum)){
			currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
			currentTraversal.addStop(stopNum);
			DfsTraverseInOrder(currentTraversal,numStops);
		}
	}
}

function BfsTraverseInOrder(traversal,numStopsSoFar,numStopsAllowed,totalNumStops){
	var currentTraversal;
	
	if(numStopsSoFar == numStopsAllowed){
		traversalArrays.push(traversal.getSequence());
	}
	else{
		for(var stopNum = 1; stopNum <= totalNumStops; stopNum++){
			if(traversal.doesNotContainStop(stopNum) && traversal.isStopGreaterThanLast(stopNum)){
				currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
				currentTraversal.addStop(stopNum);
				BfsTraverseInOrder(currentTraversal,numStopsSoFar+1,numStopsAllowed,totalNumStops);
			}
		}
	}
}

function Traversal(stopsVisitedMap,currentStepNumber){
	this.currentStopsVisitedMap = stopsVisitedMap.getClone();
	this.currentStep = currentStepNumber;
	this.timeSoFar = 0;
	this.distanceSoFar = 0; 
	
	this.addStop = function(stopNum){
		this.currentStep = this.currentStep + 1;
		this.currentStopsVisitedMap.addStop(stopNum,this.currentStep);
	}
	
	this.doesNotContainStop = function(stopNum){
		return this.currentStopsVisitedMap.DoesNotContainStop(stopNum);
	}
	
	this.isStopGreaterThanLast = function(stopNum){
		return (stopNum > this.currentStopsVisitedMap.lastStopVisited);
	}
	
	
	this.getSequence = function(){
		var theSeq = new Array();
		for(var stopNum = 1; stopNum <= this.currentStep; stopNum++){
			theSeq[stopNum-1] = this.currentStopsVisitedMap.stopsVisitedInOrder[stopNum];
		}
		return theSeq;
	}
}
function stopsVisitedHashMap(numStops){
	this.stopsVisitedInOrder = new Array();
	this.stepNumberPerStop = new Array();
	this.numberOfStops = numStops;
	this.lastStopVisited = 0;
	
	for(var index = 0; index <= numStops; index++){
		this.stopsVisitedInOrder[index] = 0;
		this.stepNumberPerStop[index] = 0;
	}
	
	this.DoesNotContainStop = function(stopNumber){
		return (this.stepNumberPerStop[stopNumber] == 0)
	}
	
	this.addStop = function(stopNumber, stepNumber){
		this.stopsVisitedInOrder[stepNumber] = stopNumber;
		this.stepNumberPerStop[stopNumber] = stepNumber;
		this.lastStopVisited = stopNumber; 
	}
	
	this.getClone = function(){
		var clone = new stopsVisitedHashMap(this.numberOfStops);
		for(var index = 0; index <= this.numberOfStops; index++){
			clone.stepNumberPerStop[index] = this.stepNumberPerStop[index];
			clone.stopsVisitedInOrder[index] = this.stopsVisitedInOrder[index];
			clone.lastStopVisited = this.lastStopVisited;
		}
		return clone;
	}
	
}

