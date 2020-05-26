package com.aim.project.pwp.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;


public class OX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public OX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	// Do nothing since OX can't be perform with only one parent
	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		double value=oSolution.getObjectiveFunctionValue();
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
		
		// A boolean array indicate whether the element is crossover part
		int size=parent1.length;
		// The size is set to be all the delivery location (except the depot and home)
		boolean[] flag1=new boolean[size];
		boolean[] flag2=new boolean[size];
		
		// Offspring that generates
		int[] Offspring1=new int[size];
		int[] Offspring2=new int[size];
		
		int times=(int) ((intensityOfMutation/0.20d>=6)?6:intensityOfMutation/0.20d);
		
		for(int z=0;z<times;z++) {
			
			// Offspring that copied from parents
			Offspring1=new int[size];
			Offspring2=new int[size];
			
			// Randomly choose two different cut-point
			int a=this.oRandom.nextInt(size);
			int b=a;
			// Indicate that a and b can't be the border at the same time
			boolean condition=(a==0&&b==size-1)||(a==size-1&&b==0); 
			while(a==b||condition) {
				b=this.oRandom.nextInt(size);
			}
			// Except the depot location at the front of parent
			int head=Math.min(a, b);
			int tail=Math.max(a, b);
			
			int i,j;
			
			for(i = head; i <= tail; i ++){
		        flag1[parent1[i]] = true;
		        flag2[parent2[i]] = true;
		        Offspring1[i] = parent1[i];
		        Offspring2[i] = parent2[i];
		    }
		    for(i = 0,j = 0; i < size;i++ ){
		        if(!flag1[parent2[i]]){
		        	// jump to the tail of offset
		            if(j == head) j = tail +1;
		            if(j<size)
		            	Offspring1[j] = parent2[i];
		            j++;
		        }
		        else{
		            flag1[parent2[i]] = false;
		        }
		    }
		    for(i = 0,j = 0; i < size;i++ ){
		        if(!flag2[parent1[i]]){
		        	// jump to the tail of offset
		            if(j == head) j = tail +1;
		            if(j<size)
			            Offspring2[j] = parent1[i];	        
		            j++;
		        }
		        else{
		            flag2[parent1[i]] = false;
		        }
		    }
		    // New parents for next round
	        parent1=(int[])Arrays.copyOf(Offspring1, Offspring1.length);
	        parent2=(int[])Arrays.copyOf(Offspring2, Offspring2.length);
		}
		boolean choose=this.oRandom.nextBoolean();
		int[] Offspring=choose? Offspring1:Offspring2;
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
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
}
