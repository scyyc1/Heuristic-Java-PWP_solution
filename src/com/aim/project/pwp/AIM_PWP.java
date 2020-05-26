package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.DavissHillClimbing;
import com.aim.project.pwp.heuristics.InversionMutation;
import com.aim.project.pwp.heuristics.NextDescent;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.heuristics.Reinsertion;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.interfaces.Visualisable;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.solution.SolutionRepresentation;

import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
			"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};

	private PWPSolutionInterface[] aoMemoryOfSolutions;

	public PWPSolutionInterface oBestSolution;

	public PWPInstanceInterface oInstance;

	private HeuristicInterface[] aoHeuristics;

	private ObjectiveFunctionInterface oObjectiveFunction;
	
	private int bestSolutionIndex;

	private final long seed;
	
	// The default settings
	private final int default_memory_size=2;
	private final double intensity=0.20d;
	private final double depthOfSearch=0.20d;
	
	private final int MUTATION_NUM = 3;
	private final int LOCAL_SEARCH_NUM = 2;
	private final int CROSSOVER_NUM = 2;

	public AIM_PWP(long seed) {

		super(seed);
		this.seed = seed;

		// TODO - set default memory size and create the array of low-level heuristics
		
		// Initialize the heuristic
		this.aoHeuristics=new HeuristicInterface[this.getNumberOfHeuristics()];
		this.aoHeuristics[0]=new InversionMutation(rng);
		this.aoHeuristics[1]=new AdjacentSwap(rng);
		this.aoHeuristics[2]=new Reinsertion(rng);
		this.aoHeuristics[3]=new NextDescent(rng);
		this.aoHeuristics[4]=new DavissHillClimbing(rng);
		this.aoHeuristics[5]=new OX(rng);
		this.aoHeuristics[6]=new CX(rng);
		
		// Randomly select a problem instance
		int size=instanceFiles.length;
		int instanceId=rng.nextInt(size);
		this.loadInstance(instanceId);
		
		// Other default settings
		this.setDepthOfSearch(this.depthOfSearch);
		this.setIntensityOfMutation(this.intensity);
		
		// Create random solution with default memory size
		for(int i=0;i<this.aoMemoryOfSolutions.length;i++)
			this.aoMemoryOfSolutions[i]=this.oInstance.createSolution(InitialisationMode.ZEROES_TO_N);
		
		// Initialize the best solution
		double temp=0;
		this.oBestSolution=aoMemoryOfSolutions[0];
		for(int i=0;i<aoMemoryOfSolutions.length;i++) {
			temp=this.aoMemoryOfSolutions[i].getObjectiveFunctionValue();
			if(temp<this.oBestSolution.getObjectiveFunctionValue()) {
				this.oBestSolution=this.aoMemoryOfSolutions[i];
				bestSolutionIndex=i;
			}
		}
	}

	public PWPSolutionInterface getSolution(int index) {
		if(index<0)
			return this.aoMemoryOfSolutions[0];
		else if(index>this.aoMemoryOfSolutions.length)
			return this.aoMemoryOfSolutions[this.aoMemoryOfSolutions.length-1];
		else
			return this.aoMemoryOfSolutions[index];
	}

	public PWPSolutionInterface getBestSolution() {return this.oBestSolution;}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {

		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		int upper=MUTATION_NUM+LOCAL_SEARCH_NUM;
		hIndex=(hIndex>=upper)?upper-1:hIndex;
		hIndex=(hIndex<0)?0:hIndex;
		HeuristicInterface h=this.aoHeuristics[hIndex];
		double value=h.apply(aoMemoryOfSolutions[candidateIndex], 
				this.getDepthOfSearch(), this.getIntensityOfMutation());
		
		this.updateBestSolution(candidateIndex);
		return value;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {

		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		int lower=MUTATION_NUM+LOCAL_SEARCH_NUM;
		int upper=lower+CROSSOVER_NUM;
		hIndex=(hIndex<lower)?lower:hIndex;
		hIndex=(hIndex>=upper)?upper-1:hIndex;
		XOHeuristicInterface h=(XOHeuristicInterface) this.aoHeuristics[hIndex];
		double value=h.apply(aoMemoryOfSolutions[parent1Index], 
				aoMemoryOfSolutions[parent2Index], 
				aoMemoryOfSolutions[candidateIndex], 
				this.getDepthOfSearch(), this.getIntensityOfMutation());
		
		this.updateBestSolution(candidateIndex);
		return value;
	}

	@Override
	public String bestSolutionToString() {
		String locations=new String("DEPOT -> ");
		int[] rep=this.oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
		for(int i=0;i<rep.length;i++) {
			locations+=String.valueOf(rep[i]);
			locations+=" -> ";
		}
		locations+="HOME";
		return locations;
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {
		double valueA=this.aoMemoryOfSolutions[iIndexA].getObjectiveFunctionValue();
		double valueB=this.aoMemoryOfSolutions[iIndexB].getObjectiveFunctionValue();
		if(valueA==valueB)return true;
		return false;
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {
		PWPSolutionInterface newSolution=this.aoMemoryOfSolutions[iIndexB].clone();
		
		// Make solutionA point to the new one
		this.aoMemoryOfSolutions[iIndexA]=newSolution;
		if(this.oBestSolution==this.aoMemoryOfSolutions[iIndexB]) {
			this.oBestSolution=this.aoMemoryOfSolutions[iIndexA];
			this.bestSolutionIndex=iIndexA;
		}
			
	}

	@Override
	public double getBestSolutionValue() {return this.oBestSolution.getObjectiveFunctionValue();}

	@Override
	public double getFunctionValue(int index) {return this.aoMemoryOfSolutions[index].getObjectiveFunctionValue();}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {

		// TODO return an array of heuristic IDs based on the heuristic's type.
		int [] list = null;
		if(type==HeuristicType.MUTATION) {
			list=new int[MUTATION_NUM];
			for(int i=0;i<MUTATION_NUM;i++) {
				list[i]=i;
			}
		}else if(type==HeuristicType.LOCAL_SEARCH) {
			list=new int[LOCAL_SEARCH_NUM];
			for(int i=0;i<LOCAL_SEARCH_NUM;i++) {
				list[i]=i+MUTATION_NUM;
			}
		}else if(type==HeuristicType.CROSSOVER) {
			list=new int[CROSSOVER_NUM];
			for(int i=0;i<CROSSOVER_NUM;i++) {
				list[i]=i+MUTATION_NUM+LOCAL_SEARCH_NUM;
			}
		}
		return list;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		ArrayList<Integer> temp=new ArrayList<Integer>();
		int j=0;
		for(int i=0;i<this.aoHeuristics.length;i++) {
			if(this.aoHeuristics[i].usesDepthOfSearch()) {
				temp.add(i);
				j++;
			}
		}
		int[] list=new int[j];
		for(int i=0;i<j;i++)
			list[i]=temp.get(i);
		return list;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		ArrayList<Integer> temp=new ArrayList<Integer>();
		int j=0;
		for(int i=0;i<this.aoHeuristics.length;i++) {
			if(this.aoHeuristics[i].usesIntensityOfMutation()) {
				temp.add(i);
				j++;
			}
		}
		int[] list=new int[j];
		for(int i=0;i<j;i++)
			list[i]=temp.get(i);
		return list;
		
	}

	@Override
	public int getNumberOfHeuristics() {

		// TODO - has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		// TODO return the number of available instances
		return 6;
	}

	@Override
	public void initialiseSolution(int index) {

		// TODO - initialise a solution in index 'index' 
		// 		making sure that you also update the best solution!
		if(this.oInstance!=null) {
			this.aoMemoryOfSolutions[index]=this.oInstance.createSolution(InitialisationMode.RANDOM);
			this.updateBestSolution(index);
		}
	}

	// TODO implement the instance reader that this method uses
	//		to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, random);

		oObjectiveFunction = oInstance.getPWPObjectiveFunction();

		for(HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}
	}

	@Override
	public void setMemorySize(int size) {
		// TODO sets a new memory size
		// IF the memory size is INCREASED, then
		//		the existing solutions should be copied to the new memory at the same indices.
		// IF the memory size is DECREASED, then
		//		the first 'size' solutions are copied to the new memory.
		if(this.aoMemoryOfSolutions==null) {
			this.aoMemoryOfSolutions=new PWPSolution[default_memory_size];
		}
		else if(size>=2) {
			PWPSolution [] new_memory=new PWPSolution[size];
			int formerSize=this.aoMemoryOfSolutions.length;
			if(size>this.aoMemoryOfSolutions.length) {
				System.arraycopy(this.aoMemoryOfSolutions,0,
						new_memory,0,
						this.aoMemoryOfSolutions.length);
				this.aoMemoryOfSolutions=new_memory;
				for(int i=formerSize;i<size;i++)
					this.oInstance.createSolution(InitialisationMode.ZEROES_TO_N);
			}else {
				System.arraycopy(this.aoMemoryOfSolutions,0,
						new_memory,0,
						size);
				this.aoMemoryOfSolutions=new_memory;
			}
		}
	}

	@Override
	public String solutionToString(int index) {
		String locations=new String("DEPOT -> ");
		int[] rep=this.aoMemoryOfSolutions[index].getSolutionRepresentation().getSolutionRepresentation();
		for(int i=0;i<rep.length;i++) {
			locations+=String.valueOf(rep[i]);
			locations+=" -> ";
		}
		locations+="HOME";
		return locations;
	}

	@Override
	public String toString() {
		return "scyyc1's G52AIM PWP";
	}

	private void updateBestSolution(int index) {
		double value=this.aoMemoryOfSolutions[index].getObjectiveFunctionValue();
		if(value<this.oBestSolution.getObjectiveFunctionValue()) {
			this.oBestSolution=this.aoMemoryOfSolutions[index];
			this.bestSolutionIndex=index;
		}
	}

	@Override
	public PWPInstanceInterface getLoadedInstance() {

		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery).toArray(Location[]::new);
		return route;
	}
	
	public int getMemorySize() {return this.aoMemoryOfSolutions.length;}
	
	public int getBestSolutionId() {return this.bestSolutionIndex;}
	
}
