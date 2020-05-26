package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public DavissHillClimbing(Random oRandom) {
	
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
		
		int size=rep.length;
		double new_value=value;
		
		ArrayList<Integer> Perturbation=new ArrayList<Integer>();
		for(int i=0;i<size;i++) {
			Perturbation.add(i);
		}
		Collections.shuffle(Perturbation);
		
		for(int i=0;i<times;i++) {
			for(int j=0;j<size;j++) {
				
				origin=(int[])Arrays.copyOf(rep, size);
				
				this.swapLocation(rep, Perturbation.get(j), 
						(Perturbation.get(j)+1)%size);
				
				// Standard evaluation
//				new_value=this.standardEvaluation(rep);
				// Delta evaluation
				new_value=this.deltaEvaluation(origin, rep, value);
					
				
				// If a better solution is found, accept it and record the value
				if(new_value<value) {
					value=new_value;
				} // If not found, switch back to the origin
				else {
					this.swapLocation(rep, Perturbation.get(j), 
							(Perturbation.get(j)+1)%size);
				}
			}
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
