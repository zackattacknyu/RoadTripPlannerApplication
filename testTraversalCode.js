var traversalStrings = new Array();

function renderOptions(){
	$("#TestString").html("Traversals are: </br>" + TestHash());
}

function TestHash(){
	DoDfsSearch(3);
	for(var ind = 0; ind < traversalStrings.length; ind++){
		console.log(traversalStrings[ind]);
	}
	//console.log("Now onto DFS");
	//DoDfsSearch(3);
	var resultStr = "Finished";
	return resultStr;
}

function getTraversalString(traversal){
	var str = "";
	var traversalSequence = traversal.getSequence();
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

function DfsTraverse(traversal,numStops){
	traversalStrings.push(getTraversalString(traversal));
	var currentTraversal;
	
	for(var stopNum = 1; stopNum <= numStops; stopNum++){
		if(traversal.currentStopsVisitedMap.DoesNotContainStop(stopNum)){
			currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
			currentTraversal.addStop(stopNum);
			DfsTraverse(currentTraversal,numStops);
		}
	}
}

function BfsTraverse(traversal,numStopsSoFar,numStopsAllowed,totalNumStops){
	var currentTraversal;
	
	if(numStopsSoFar == numStopsAllowed){
		traversalStrings.push(getTraversalString(traversal));
	}
	else{
		for(var stopNum = 1; stopNum <= totalNumStops; stopNum++){
			if(traversal.currentStopsVisitedMap.DoesNotContainStop(stopNum)){
				currentTraversal = new Traversal(traversal.currentStopsVisitedMap,traversal.currentStep);
				currentTraversal.addStop(stopNum);
				BfsTraverse(currentTraversal,numStopsSoFar+1,numStopsAllowed,totalNumStops);
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
	}
	
	this.getClone = function(){
		var clone = new stopsVisitedHashMap(this.numberOfStops);
		for(var index = 0; index <= this.numberOfStops; index++){
			clone.stepNumberPerStop[index] = this.stepNumberPerStop[index];
			clone.stopsVisitedInOrder[index] = this.stopsVisitedInOrder[index];
		}
		return clone;
	}
	
}

