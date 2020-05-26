package com.aim.project.pwp.runners;

import com.aim.project.pwp.hyperheuristics.SR_ME_HH;

import AbstractClasses.HyperHeuristic;

public class SR_ME_runner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new SR_ME_HH(seed, 3, 2, 2);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new SR_IE_VisualRunner();
		runner.run();
	}
}
