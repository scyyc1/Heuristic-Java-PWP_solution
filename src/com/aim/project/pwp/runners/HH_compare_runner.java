package com.aim.project.pwp.runners;

import java.awt.Color;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;
import com.aim.project.pwp.hyperheuristics.SR_ME_HH;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.visualiser.PWPView;

import AbstractClasses.HyperHeuristic;

public class HH_compare_runner extends HH_Runner_Visual {
	
	private static int trials=11;

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {
		return null;
	}
	
	public static void main(String [] args) {
		long seed = 130320201;
		long timeLimit = 6000;
		HyperHeuristic hh=new SR_ME_HH(seed, 3, 2, 2);
//		HyperHeuristic hh=new SR_IE_HH(seed);
		hh.setTimeLimit(timeLimit);	
		
		double min=0;
		double sum=0;
		double average=0;
		
		for(int i=0;i<trials;i++) {
			double temp=HH(hh, i+1);
			if(i==0)min=temp;
			sum+=temp;
			if(temp<min)
				min=temp;
		}
			
		average=sum/trials;
		System.out.println("Average fitness of " + hh.toString() + " is " + average);
		System.out.println("Best fitness of " + hh.toString() + " is " + min);
	}

	public static double HH(HyperHeuristic hh, int round) {
		
		long seed = 130320201;
		AIM_PWP problem = new AIM_PWP(seed);
//		problem.loadInstance(2);
		problem.loadInstance(3);
//		problem.loadInstance(4);
		hh.loadProblemDomain(problem);	
		
		hh.run();	
		
		PWPSolution p=(PWPSolution) problem.getBestSolution();		
		
		System.out.println("Best solution at trail "+round+" is "+problem.getBestSolutionValue());
		new PWPView(problem.oInstance, problem, Color.RED, Color.GREEN);	
		String temp1=problem.bestSolutionToString();
		System.out.println(temp1);
		
		return problem.getBestSolutionValue();
		

	}
}
