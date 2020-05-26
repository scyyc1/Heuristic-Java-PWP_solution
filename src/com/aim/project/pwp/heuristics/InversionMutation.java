package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;


public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public InversionMutation(Random oRandom) {
	
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
		
		int times=(int) ((dIntensityOfMutation/0.20d>=6)?6:dIntensityOfMutation/0.20d);
		
		for(int i=0;i<times;i++) {
			// Randomly choose 2 different position
			int l1=this.oRandom.nextInt(rep.length);
			int l2=l1;
			while(l1==l2) {
				l2=this.oRandom.nextInt(rep.length);
			}
			
			int head=Math.min(l1, l2);
			int tail=Math.max(l1, l2);
			
			// Swap the head & tail and moving to the center of the selected fragment 
			// Obtain a inversion fragment
			while(head<tail) {
				this.swapLocation(rep, head, tail);
				head++;
				tail--;
			}
		}
		// Standard evaluation
//		value=this.standardEvaluation(rep);
		// Delta evaluation
		value=this.deltaEvaluation(origin, rep, value);
		
		oSolution.setObjectiveFunctionValue(value);
		
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
