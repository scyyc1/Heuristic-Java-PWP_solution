package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;


public class CX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		double value=solution.getObjectiveFunctionValue();
		return value;
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		// Parents representation
		int[] parent1=(int[])Arrays.copyOf(
				p1.getSolutionRepresentation().getSolutionRepresentation(),
				p1.getNumberOfLocations()-2);
		int[] parent2=(int[])Arrays.copyOf(
				p2.getSolutionRepresentation().getSolutionRepresentation(),
				p2.getNumberOfLocations()-2);
		int size=parent1.length;
		
		int times=(int) ((intensityOfMutation/0.20d>=6)?6:intensityOfMutation/0.20d);
		
		int[] Offspring1=(int[])Arrays.copyOf(parent1, parent1.length);
		int[] Offspring2=(int[])Arrays.copyOf(parent2, parent2.length);
		
		for(int z=0;z<times;z++) {
			
			// Offspring that copied from parents
			Offspring1=(int[])Arrays.copyOf(parent1, parent1.length);
			Offspring2=(int[])Arrays.copyOf(parent2, parent2.length);

			// Generate starting point
			int idx=this.oRandom.nextInt(size);
	
	        // the indices of the current cycle
	        final List<Integer> indices = new ArrayList<Integer>(size);
        
        	// The first one
            indices.add(idx);
            idx=this.searchIndex(parent1, parent2[idx]);

            while (idx != indices.get(0)) {
                indices.add(idx);
                idx=this.searchIndex(parent1, parent2[idx]);
            }

            // Swap the element found in Offspring, only one cycle
            for (int i : indices) {
                int temp=Offspring1[i];
                Offspring1[i]=Offspring2[i];
                Offspring2[i]=temp;
            }
            
            // New parents for next round
	        parent1=(int[])Arrays.copyOf(Offspring1, Offspring1.length);
	        parent2=(int[])Arrays.copyOf(Offspring2, Offspring2.length);
        }
		boolean choose=this.oRandom.nextBoolean();
		int[] Offspring=choose? (int[])Arrays.copyOf(Offspring1, Offspring1.length):(int[])Arrays.copyOf(Offspring2, Offspring2.length);
        double value=this.oObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(Offspring));
        
        c.setObjectiveFunctionValue(value);
	    c.getSolutionRepresentation().setSolutionRepresentation(Offspring);
	    
		return value;
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}
	
	// Return the index of the value n in the array
	private int searchIndex(int[] rep, int n) {
		for(int i=0;i<rep.length;i++) {
			if(rep[i]==n)return i;
		}
		return -1;
	}
}
