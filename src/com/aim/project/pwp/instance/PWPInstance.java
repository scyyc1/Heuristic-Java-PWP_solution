package com.aim.project.pwp.instance;


import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.PWPObjectiveFunction;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.pwp.solution.PWPSolution;


public class PWPInstance implements PWPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oPostalDepotLocation;
	
	private final Location oHomeAddressLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction = null;
	
	/**
	 * 
	 * @param numberOfLocations The TOTAL number of locations (including DEPOT and HOME).
	 * @param aoLocations The delivery locations.
	 * @param oPostalDepotLocation The DEPOT location.
	 * @param oHomeAddressLocation The HOME location.
	 * @param random The random number generator to use.
	 */
	public PWPInstance(int numberOfLocations, Location[] aoLocations, Location oPostalDepotLocation, Location oHomeAddressLocation, Random random) {
		
		this.iNumberOfLocations = numberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oPostalDepotLocation = oPostalDepotLocation;
		this.oHomeAddressLocation = oHomeAddressLocation;
	}

	@Override
	public PWPSolution createSolution(InitialisationMode mode) {
		int[] rep=new int[this.iNumberOfLocations];
		if(mode==InitialisationMode.RANDOM) {
			// TODO construct a new 'PWPSolution' using RANDOM initialization
			// Create a perturbation list
			rep=this.PWPperturbationInitializer(this.iNumberOfLocations);
			
		}else if(mode==InitialisationMode.ZEROES_TO_N) {
			// Ordered list
			for(int i=0;i<this.iNumberOfLocations;i++)
				rep[i]=i;
			
		}else {
			// Ordered list
			for(int i=0;i<this.iNumberOfLocations;i++)
				rep[i]=i;
		}
		SolutionRepresentation n=new SolutionRepresentation(rep);
		double value=this.oObjectiveFunction.getObjectiveFunctionValue(n);
		return new PWPSolution(n, value);
	}
	
	@Override
	public ObjectiveFunctionInterface getPWPObjectiveFunction() {
		
		if(oObjectiveFunction == null) {
			this.oObjectiveFunction = new PWPObjectiveFunction(this);
		}

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

		return iNumberOfLocations;
	}

	@Override
	public Location getLocationForDelivery(int deliveryId) {

		return aoLocations[deliveryId];
	}

	@Override
	public Location getPostalDepot() {
		
		return this.oPostalDepotLocation;
	}

	@Override
	public Location getHomeAddress() {
		
		return this.oHomeAddressLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(PWPSolutionInterface oSolution) {
		
		// TODO return an 'ArrayList' of ALL LOCATIONS in the solution.
		int [] list=oSolution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Location> locationList=new ArrayList<Location>();
		for(int i=0;i<list.length;i++) 
			locationList.add(this.getLocationForDelivery(i));
		
		return locationList;
	}
	
	// Initialize a perturbation list which suits the PWP problem
	public int[] PWPperturbationInitializer(int size) {
		int[] p=new int[size];
		// Ordered list
		for(int i=0;i<size;i++)
			p[i]=i;
		// Shuffle the list
		for(int i=size;i>0;i--) {
			int rand=this.oRandom.nextInt(i);
			// Swap the with the number whose index is randomly generated
			int temp=p[i-1];
			p[i-1]=p[rand];
			p[rand]=temp;
		}
		return p;
	}
}
