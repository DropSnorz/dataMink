package com.dropsnorz.datamink.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.springframework.util.StringUtils;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class ProgramEvaluator {

	Mapping mapping;
	HashMap<String, ArrayList<String[]>> baseFacts;
	HashMap<String, ArrayList<String[]>> computedFacts;
	ArrayList<String> constants;



	public ProgramEvaluator(Mapping mapping){
		this.mapping = mapping;


	}

	public void evaluation(){

		computeBaseFacts();
		//DEBUG

		String output = "---Base Facts--- \n";
		for(String key : baseFacts.keySet()){
			for(String[] list : baseFacts.get(key)){
				output += key + "(" + StringUtils.arrayToCommaDelimitedString(list) +")\n";
			}
		}

		System.out.println(output);


		//END OF DEBUG

		boolean loop = true;
		while(loop){
			loop = false;
			for(Tgd t : mapping.getTgds()){

				ArrayList<String> varList = new ArrayList<String>();

				for(Literal l : t.getLeft()){
					for(Variable v : l.getAtom().getVars()){
						if(!varList.contains(v.getName())){
							varList.add(v.getName());
						}
					}

				}

				int nVars = varList.size();	
				String[] varArray = new String [varList.size()];
				varList.toArray(varArray);

				String[] map = new String[constants.size()];

				int i = 0;
				for(String s: constants){
					map[i] = s;
					i++;
				}

				String[][] groundings = getAllLists(map,nVars);

				for(String[] set: groundings){

					//System.out.println(StringUtils.arrayToCommaDelimitedString(set));
					if (evalTgdInstance(t, set, varArray)){

						//All body clauses are positive, we check to add a fact					
						if(addOrRejectComputedFact(t, set, varArray)){
							loop = true;
						}

					}
				}

			}
		}

	}


	public void stratifiedEvaluation(StratifiedDatalogProgram program){

		computeBaseFacts();


		ArrayList<ArrayList<Object>> partitions = program.getPartitions();

		for(ArrayList<Object> partition : partitions){
			sliceEvaluation(partition);
		}

	}

	public void sliceEvaluation(ArrayList<Object> partition){

		boolean loop = true;
		while(loop){
			loop = false;

			for(Object object : partition){

				if(object instanceof Tgd){

					ArrayList<String> varList = new ArrayList<String>();
					Tgd t = (Tgd) object;

					for(Literal l : t.getLeft()){
						for(Variable v : l.getAtom().getVars()){
							if(!varList.contains(v.getName())){
								varList.add(v.getName());
							}
						}

					}

					int nVars = varList.size();	
					String[] varArray = new String [varList.size()];
					varList.toArray(varArray);

					String[] map = new String[constants.size()];

					int i = 0;
					for(String s: constants){
						map[i] = s;
						i++;
					}

					String[][] groundings = getAllLists(map,nVars);

					for(String[] set: groundings){

						//System.out.println(StringUtils.arrayToCommaDelimitedString(set));
						if (evalTgdInstance(t, set, varArray)){

							//All body clauses are positive, we check to add a fact					
							if(addOrRejectComputedFact(t, set, varArray)){
								loop = true;
							}

						}
					}
				}
			}
		}
	}


	public void computeBaseFacts(){
		baseFacts = new HashMap<String, ArrayList<String[]>>();
		computedFacts = new HashMap<String, ArrayList<String[]>>();

		constants = new ArrayList<String>();

		//Generate base facts from EDB
		for(Relation r : mapping.getEDB()){

			for(String c : r.getAttributes() ){
				if(!constants.contains(c)){
					constants.add(c);
				}
			}

			if(!baseFacts.containsKey(r.getName())){
				ArrayList<String[]>list = new ArrayList<String[]>();
				list.add(r.getAttributes());
				baseFacts.put(r.getName(), list);
			}
			else{
				baseFacts.get(r.getName()).add(r.getAttributes());
			}
		}

	}
	public static String[][] getAllLists(String[] elements, int lengthOfList)
	{
		//initialize our returned list with the number of elements calculated above
		String[][] allLists = new String[(int)Math.pow(elements.length, lengthOfList)][elements.length];

		//lists of length 1 are just the original elements
		if(lengthOfList == 1){
			String[][] output = new String[elements.length][lengthOfList];
			for(int i =0; i < elements.length; i ++){
				output[i] = new String[1];
				output[i][0] = elements[i];
			}

			return output;
		}
		else
		{
			//the recursion--get all lists of length 3, length 2, all the way up to 1
			String[][] allSublists = getAllLists(elements, lengthOfList - 1);

			//append the sublists to each element
			int arrayIndex = 0;

			for(int i = 0; i < elements.length; i++)
			{
				for(int j = 0; j < allSublists.length; j++)
				{
					//add the newly appended combination to the list
					allLists[arrayIndex] = new String[lengthOfList];
					allLists[arrayIndex][0] = elements[i];
					for(int k=0; k < lengthOfList - 1; k++){
						allLists[arrayIndex][k + 1] = allSublists[j][k];
					}

					arrayIndex++;
				}
			}

			return allLists;
		}
	}


	public boolean evalTgdInstance(Tgd t, String[] map, String[] vars){

		for(Literal l : t.getLeft()){
			if(!evalLiteralInstance(l, map, vars)){
				return false;
			}
		}

		return true;
	}

	public boolean evalLiteralInstance(Literal l, String[] map, String[] vars){

		ArrayList<String[]> baseFactsInstanceList = baseFacts.get(l.getAtom().getName());
		ArrayList<String[]> computedFactsInstanceList = computedFacts.get(l.getAtom().getName());

		String[] currentVars = new String[l.getAtom().getVars().size()];

		int j = 0;
		for(Variable v : l.getAtom().getVars()){
			currentVars[j] = v.getName();
			j++;
		}

		String[] projection = new String[currentVars.length];


		for(int i = 0; i < currentVars.length; i++){
			projection[i] = getInstanceValue(currentVars[i], map, vars);
		}
		
		if(baseFactsInstanceList != null){
			for(String [] instance : baseFactsInstanceList){
				if(Arrays.deepEquals(projection, instance) && l.getFlag()){
					return true;
				}
				else if (Arrays.deepEquals(projection, instance) && !l.getFlag()){
					return false;
				}
			}
		}

		if(computedFactsInstanceList != null){
			for(String [] instance : computedFactsInstanceList){
				if(Arrays.deepEquals(projection, instance) && l.getFlag()){
					return true;
				}
				else if (Arrays.deepEquals(projection, instance) && !l.getFlag()){
					return false;
				}
			}
		}

		if(l.getFlag()){
			return false;

		}
		else{
			return true;
		}
	}

	private String getInstanceValue(String arg, String[] map, String[] vars){

		for(int i = 0; i < vars.length; i ++){
			if(vars[i].equals(arg)){
				return map[i];
			}
		}
		System.out.println("Error ! looking for " + arg);
		return null;
	}

	public boolean addOrRejectComputedFact(Tgd t, String[] map, String[] vars){

		String[] currentVars = new String[t.getRight().getVars().size()];

		int j = 0;
		for(Variable v : t.getRight().getVars()){
			currentVars[j] = v.getName();
			j++;
		}

		String[] projection = new String[currentVars.length];

		for(int k = 0; k < currentVars.length; k++){
			projection[k] = getInstanceValue(currentVars[k], map, vars);
		}

		ArrayList<String[]> computedVariablesList = computedFacts.get(t.getRight().getName());

		if (computedVariablesList != null){

			for(String[] computedInstance : computedVariablesList){
				if(Arrays.deepEquals(computedInstance, projection)){
					return false;
				}
			}

			computedFacts.get(t.getRight().getName()).add(projection);

			return true;


		}
		else{
			computedVariablesList = new ArrayList<String[]>();
			computedVariablesList.add(projection);
			computedFacts.put(t.getRight().getName(), computedVariablesList);

			return true;
		}


	}


	public String getComputedFactsDisplay(){

		String output = "";
		for(String key : computedFacts.keySet()){
			for(String[] list : computedFacts.get(key)){
				output += key + "(" + StringUtils.arrayToCommaDelimitedString(list) +")\n";
			}
		}

		return output;
	}


}
