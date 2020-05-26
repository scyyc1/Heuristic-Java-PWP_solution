package com.aim.project.pwp.testScript;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aim.project.pwp.AIM_PWP;

class DavissHillClimbing_test {

	private AIM_PWP p;
	private int index_size=10;
	
	@BeforeEach
	void setUp() throws Exception {
		this.p=new AIM_PWP(1111);
		Random rng=new Random(1111);
	}

	@Test
	void test() {
		p.setIntensityOfMutation(0.20d);
		System.out.println(p.solutionToString(0));
		System.out.println(p.getFunctionValue(0));
		p.applyHeuristic(4, 1, 0);
		System.out.println(p.solutionToString(0));
		System.out.println(p.getFunctionValue(0));
	}

}
