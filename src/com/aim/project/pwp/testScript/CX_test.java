package com.aim.project.pwp.testScript;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.solution.SolutionRepresentation;

class CX_test {

	private AIM_PWP p;
	private int index_size=10;
	
	@BeforeEach
	void setUp() throws Exception {
		this.p=new AIM_PWP(1111);
		Random rng=new Random(1111);
	}


	@Test
	void test() {
		p.setMemorySize(index_size);
//		for(int i=0;i<index_size;i++)
//			System.out.println(p.solutionToString(i));
		System.out.println();
		p.applyHeuristic(6, 2, 8, 1);
		System.out.println(p.solutionToString(2));
		System.out.println(p.solutionToString(8));
		System.out.println(p.solutionToString(1));
	}	

}
