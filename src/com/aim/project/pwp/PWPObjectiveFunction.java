package com.aim.project.pwp;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		double sum=0;
		int[] representation=oSolution.getSolutionRepresentation();
		int size=oSolution.getNumberOfLocations()-2;
		
		// Calculate the sum of a solution
		sum+=this.getCostBetweenDepotAnd(representation[0]);
		for(int i=1;i<size;i++)
			sum+=this.getCost(representation[i], representation[i-1]);
		sum+=this.getCostBetweenHomeAnd(representation[size-1]);
		
		return sum;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		Location l1=oInstance.getLocationForDelivery(iLocationA);
		Location l2=oInstance.getLocationForDelivery(iLocationB);
		
		return Math.sqrt(Math.pow((l1.getX()-l2.getX()), 2)
				+ Math.pow((l1.getY()-l2.getY()), 2));
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		Location Depot=oInstance.getPostalDepot();
		Location l=oInstance.getLocationForDelivery(iLocation);
		
		return Math.sqrt(Math.pow((l.getX()-Depot.getX()), 2)
				+ Math.pow((l.getY()-Depot.getY()), 2));
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		Location Home=oInstance.getHomeAddress();
		Location l=oInstance.getLocationForDelivery(iLocation);
		
		return Math.sqrt(Math.pow((l.getX()-Home.getX()), 2)
				+ Math.pow((l.getY()-Home.getY()), 2));
	}	
}
