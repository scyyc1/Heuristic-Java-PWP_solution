package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class hahaha extends HyperHeuristic {

public hahaha(long Seed) {
		
		super(Seed);
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		int currentId=0;
		int candidateId=1;
		int anotherParent=2;
		oProblem.setMemorySize(3);
		
		oProblem.initialiseSolution(currentId);
		oProblem.initialiseSolution(candidateId);
		double current = oProblem.getFunctionValue(currentId);
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 0;
		long iteration = 0;
		boolean accept;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		while(!hasTimeExpired() ) {
			hasTimeExpired();
			h = rng.nextInt(oProblem.getNumberOfHeuristics());
			
			double candidate;
			if(h < 5) {
				candidate = oProblem.applyHeuristic(h, currentId, candidateId);
			} else {
				oProblem.initialiseSolution(2);
				candidate = oProblem.applyHeuristic(h, currentId, anotherParent, candidateId);
			}
			
			accept = candidate <= current;
			if(accept) {
				
				oProblem.copySolution(currentId, candidateId);
				current = candidate;
			}
			
			iteration++;
		}
		
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		System.out.println(String.format("Total iterations = %d", iteration));
	}

	@Override
	public String toString() {

		return "SR_IE_HH";
	}

}
