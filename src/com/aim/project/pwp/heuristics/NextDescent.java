package com.aim.project.pwp.heuristics;


import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public NextDescent(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		SolutionRepresentationInterface representation=oSolution.getSolutionRepresentation();
		int [] rep=representation.getSolutionRepresentation();
		int [] origin=new int[rep.length];
		System.arraycopy(rep, 0, origin, 0, rep.length);
		double value=oSolution.getObjectiveFunctionValue();
		
		int times=(int) ((dDepthOfSearch/0.20d>=6)?6:dDepthOfSearch/0.20d);
		
		int accepted=0;
		int locationNum=rep.length;
		int chosenLocation=0;
		double new_value=0;
		
		while(accepted<times&&chosenLocation<locationNum) {
			int next=(chosenLocation+1)%locationNum;
			this.swapLocation(rep, chosenLocation, next);
			
			// Standard evaluation
//			 value=this.standardEvaluation(rep);
			// Delta evaluation
			new_value=this.deltaEvaluation(origin, rep, value);
			
			// If a better solution is found, accept it and record the value
			if(new_value<value) {
				value=new_value;
				accepted++;
			}
			// If not found, switch back to the origin
			else {
				this.swapLocation(rep, chosenLocation, next);
			}
			
			chosenLocation++;
		}
		oSolution.setObjectiveFunctionValue(value);

		return value;
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
}
