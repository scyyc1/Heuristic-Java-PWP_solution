package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		SolutionRepresentationInterface representation=solution.getSolutionRepresentation();
		int [] rep=representation.getSolutionRepresentation();
		int [] origin=new int[rep.length];
		System.arraycopy(rep, 0, origin, 0, rep.length);
		double value=solution.getObjectiveFunctionValue();
		
		int times=(int) ((intensityOfMutation/0.20d>=6)?6:intensityOfMutation/0.20d);
		
		for(int i=0;i<times;i++) {
			// Randomly choose 2 position
			int chosenLocation=this.oRandom.nextInt(rep.length);
			int insertLocation=chosenLocation;
			while(chosenLocation==insertLocation) {
				insertLocation=this.oRandom.nextInt(rep.length);
			}
			// Swap with the next element until target element reaches the target position
			if(insertLocation>chosenLocation)
				for(int j=chosenLocation;j<insertLocation;j++)
					this.swapLocation(rep, j, j+1);
			else
				for(int j=chosenLocation;j>insertLocation;j--)
					this.swapLocation(rep, j, j-1);
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
