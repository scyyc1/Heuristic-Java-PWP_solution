package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class SR_ME_HH extends HyperHeuristic {
	
	private int PopulationSize=5;
	private int OffspringSize=1;
	
	private int mutationNum;
	private int localSearchNum;
	private int crossoverNum;

	public SR_ME_HH(long Seed, int mutationNum, int localSearchNum, int crossoverNum) {
			
		super(Seed);
		this.localSearchNum=localSearchNum;
		this.mutationNum=mutationNum;
		this.crossoverNum=crossoverNum;
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		oProblem.setMemorySize(PopulationSize+OffspringSize);
		for(int i=0;i<PopulationSize+OffspringSize;i++) {
			oProblem.initialiseSolution(i);
		}

		double current;
		double candidate;
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 0;
		long iteration = 0;
		boolean accept;

		while(!hasTimeExpired() ) {
			
			int currentId=rng.nextInt(PopulationSize);
			int candidateId=PopulationSize;
			int anotherParent=currentId;
			while(anotherParent==currentId)
				anotherParent=rng.nextInt(PopulationSize);
			
			current=oProblem.getFunctionValue(currentId);
			
			h=rng.nextInt(crossoverNum)+mutationNum+localSearchNum;
			oProblem.applyHeuristic(h, currentId, anotherParent, candidateId);
			h=rng.nextInt(mutationNum);
			oProblem.applyHeuristic(h, candidateId, candidateId);
			h=rng.nextInt(localSearchNum)+mutationNum;
			candidate=oProblem.applyHeuristic(h, candidateId, candidateId);
			
			accept = candidate <= current;
			if(accept) {
				oProblem.copySolution(currentId, candidateId);
				current = candidate;
			}else {
				current=oProblem.getFunctionValue(currentId);
			}
			
			iteration++;
		}
		
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
//		System.out.println(String.format("Total iterations = %d", iteration));
	}

	public void setPopulationSize(int size) {this.PopulationSize=size;}
	public int getPopulationSize() {return this.PopulationSize;}
	@Override
	public String toString() {

		return "SR_ME_HH";
	}
	
}
