package com.aim.project.pwp.testScript;

import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aim.project.pwp.AIM_PWP;

class AdjacentSwap_test {

	private AIM_PWP p;
	
	@BeforeEach
	void setUp() throws Exception {
		this.p=new AIM_PWP(1111);
	}

	@Test
	void test() {
		p.setIntensityOfMutation(0.10d);
		System.out.println(p.solutionToString(0));
		System.out.println(p.getFunctionValue(0));
		double origin=p.getFunctionValue(0);
		p.applyHeuristic(1, 1, 0);
		System.out.println(p.solutionToString(0));
		System.out.println(p.getFunctionValue(0));
		double n=p.getFunctionValue(0);
		assertFalse(n==origin);
	}

}
