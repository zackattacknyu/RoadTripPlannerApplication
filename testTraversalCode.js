function renderOptions(){
	$("#TestString").html("Traversals are: </br>" + TestHash());
}

function TestHash(){
	var stopsHash = new stopsVisitedHashMap(3);
	stopsHash.addStop(2,1);
	stopsHash.addStop(3,2);
	stopsHash.addStop(1,3);
	var sampletraverse = new Traversal(stopsHash,1);
	var resultStr = getTraversalString(sampletraverse);
	return resultStr;
}

function getTraversalString(traversal){
	var str = "";
	for(var ind = 1; ind <= traversal.currentStopsVisitedMap.numberOfStops; ind++){
		str += traversal.currentStopsVisitedMap.stopsVisitedInOrder[ind] + " - ";
	}
	return str;
}

function Traversal(stopsVisitedMap,currentStepNumber){
	this.currentStopsVisitedMap = stopsVisitedMap.getClone();
	this.currentStep = currentStepNumber;
	this.timeSoFar = 0;
	this.distanceSoFar = 0; 
	
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

