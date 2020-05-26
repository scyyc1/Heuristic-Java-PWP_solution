package com.aim.project.pwp.solution;

import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {

		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {

		// TODO return the total number of locations in this instance (includes DEPOT and HOME).
		return this.getSolutionRepresentation().length+2;
	}

	@Override
	public SolutionRepresentationInterface clone() {

		// TODO perform a DEEP clone of the solution representation! \18:12 modify
		int [] newCopy=new int[this.aiSolutionRepresentation.length];
		System.arraycopy(this.aiSolutionRepresentation, 0, newCopy, 0, newCopy.length);
		return new SolutionRepresentation(newCopy);
	}

}
