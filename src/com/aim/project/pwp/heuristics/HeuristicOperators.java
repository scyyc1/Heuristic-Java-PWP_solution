package com.aim.project.pwp.heuristics;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;

public class HeuristicOperators {

	protected ObjectiveFunctionInterface oObjectiveFunction;
	
	// Since "ObjectiveFunctionInterface" needs instance
	private PWPInstanceInterface oInstance;
	
	public HeuristicOperators() {}
	
	// Constructor with instance and objective function initialized
	public HeuristicOperators(PWPInstanceInterface oInstance, 
			ObjectiveFunctionInterface oObjectiveFunction) {
		this.oInstance=oInstance;
		this.oObjectiveFunction=this.oInstance.getPWPObjectiveFunction();
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.oObjectiveFunction = f;
	}

	/**
	 * TODO implement any common functionality here so that your
	 * 			heuristics can reuse them!
	 * E.g.  you may want to implement the swapping of two delivery locations here!
	 */
	public void swapLocation(int [] rep, int id1, int id2){
		int temp=rep[id1];
		rep[id1]=rep[id2];
		rep[id2]=temp;
	}
	
	// Standard calculation of objective value (4.4)
	public double standardEvaluation(int[] newList) {
		return this.oObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(newList));
	}

	// Delta evaluation of objective value (4.4.1)
	public double deltaEvaluation(int[] oldList, int[] newList, double value) {
		if(oldList==null||newList==null)return value;
		if(oldList.length!=newList.length)return value;
		int size=oldList.length;
		for(int i=0;i<size;i++) {
			if(oldList[i]!=newList[i]) {
				int j=i;
				if(j==0) {
					value-=this.oObjectiveFunction.getCostBetweenDepotAnd(oldList[j]);
					value+=this.oObjectiveFunction.getCostBetweenDepotAnd(newList[j]);
					j++;
				}
				while(oldList[j]!=newList[j]) {
					value-=this.oObjectiveFunction.getCost(oldList[j], oldList[j-1]);
					value+=this.oObjectiveFunction.getCost(newList[j], newList[j-1]);
					j++;
					if(j>=size)break;
				}
				if(j==size) {
					value-=this.oObjectiveFunction.getCostBetweenHomeAnd(oldList[j-1]);
					value+=this.oObjectiveFunction.getCostBetweenHomeAnd(newList[j-1]);
				}else {
					value-=this.oObjectiveFunction.getCost(oldList[j], oldList[j-1]);
					value+=this.oObjectiveFunction.getCost(newList[j], newList[j-1]);
				}
				i=j;
			}
		}
		return value;
	}
}
