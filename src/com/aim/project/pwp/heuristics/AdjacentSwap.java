package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public AdjacentSwap(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}
	
	public AdjacentSwap(PWPInstanceInterface oInstance, 
			ObjectiveFunctionInterface oObjectiveFunction,
			Random oRandom) {
		super(oInstance, oObjectiveFunction);
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		SolutionRepresentationInterface representation=solution.getSolutionRepresentation();
		int [] rep=representation.getSolutionRepresentation();
		int [] origin=new int[rep.length];
		System.arraycopy(rep, 0, origin, 0, rep.length);
		double value=solution.getObjectiveFunctionValue();
		
		int index=(int) (intensityOfMutation/0.20d);
		if(index>5)index=5;
		int times=(int) Math.pow(2, index);

		for(int i=0;i<times;i++) {
			// Randomly choose location except the depot and home
			int chosenId=this.oRandom.nextInt(rep.length);
			
			// If the last location is chosen, swap it with the first
			int next=(chosenId+1)%(rep.length);
			this.swapLocation(rep, chosenId, next);
		}
		
		// Standard evaluation
//		value=this.standardEvaluation(rep);
		// Delta evaluation
		value=this.deltaEvaluation(origin, rep, value);
		
		solution.setObjectiveFunctionValue(value);
		
		return value;
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}

}

