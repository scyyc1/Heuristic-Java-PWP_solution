package com.aim.project.pwp.testScript;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aim.project.pwp.AIM_PWP;

class AIM_PWP_Functional_test {

	private AIM_PWP p;
	private int index_size=10;
	private int seed=10;
	private Random rng;
	
	@BeforeEach
	void setUp() throws Exception {
		this.p=new AIM_PWP(seed);
		this.rng=new Random(seed);
	}

	@Test
	void test() {
		// Check if the framework automatically keeps track of the best solution
		p.setMemorySize(index_size);
		double best=p.getBestSolutionValue();
		int size=p.getMemorySize();
		double greatest=p.getFunctionValue(0);
		for(int i=0;i<size;i++) {
			if(p.getFunctionValue(i)<greatest) {
				greatest=p.getFunctionValue(i);
			}
		}
		assertEquals(best, greatest);

		// Check if the copy function work
		int testExampleA=rng.nextInt(index_size);
		int testExampleB=testExampleA;
		while(testExampleB==testExampleA)
			testExampleB=rng.nextInt(index_size);
		assertFalse(p.getFunctionValue(testExampleA)==p.getFunctionValue(testExampleB));
		p.copySolution(testExampleA, testExampleB);
		assertEquals(p.getFunctionValue(testExampleA), p.getFunctionValue(testExampleB));
		// Check if the compareSolution works
		assertTrue(p.compareSolutions(testExampleA, testExampleB));
		assertFalse(p.getSolution(testExampleA)==p.getSolution(testExampleB));
		for(int i=0;i<p.getSolution(testExampleA).getNumberOfLocations();i++) {
			int a=p.getSolution(testExampleA).getSolutionRepresentation().getSolutionRepresentation()[i];
			int b=p.getSolution(testExampleB).getSolutionRepresentation().getSolutionRepresentation()[i];
			assertEquals(a,b);
		}
		
		// Check if the modification to copied solution won't affect the former one
		p.initialiseSolution(testExampleA);
		assertFalse(p.getFunctionValue(testExampleA)==p.getFunctionValue(testExampleB));
		assertFalse(p.getSolution(testExampleA)==p.getSolution(testExampleB));
		
	}

}
