package com.dropsnorz.datamink.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import fr.univlyon1.mif37.dex.mapping.AbstractRelation;
import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class DatalogProgram {

	protected Mapping mapping;

	public DatalogProgram(Mapping mapping){

		this.mapping = mapping;

	}

	public ProgramType getProgramType(){


		if(isPositive()) return ProgramType.POSITIVE;
		if(isSemiPositive()) return ProgramType.SEMI_POSITIVE;


		//Check for positive datalog program
		boolean stratifiable = true;

		DirectedGraph<String, SignedEdge> graph = generateMappingGraph();

		CycleDetector<String, SignedEdge> cycleDetector = new CycleDetector<String, SignedEdge>(graph);
		Set<String> nodesInCycle = cycleDetector.findCycles();


		for(String node : nodesInCycle){
			for(String checkNode : nodesInCycle){
				if(checkNode.equals(node)){

					if(graph.containsEdge(node, checkNode)){
						SignedEdge edge = graph.getEdge(node, checkNode);

						if(!edge.getSign()){
							stratifiable = false;
						}
					}
				}
			}
		}

		if(stratifiable){
			return ProgramType.STRATIFIABLE;

		}
		else{
			return ProgramType.UNKNOW;
		}
	}

	private boolean isPositive(){
		for(Tgd t : mapping.getTgds()){
			for(Literal l : t.getLeft()){
				if(!l.getFlag()){
					return false;
				}
			}

		}

		return true;
	}

	private boolean isSemiPositive(){
		boolean semiPositive = false;
		for(Tgd t : mapping.getTgds()){
			for(Literal l : t.getLeft()){
				if(!l.getFlag()){
					semiPositive = false;
					for(Relation r : mapping.getEDB()){
						if(r.getName().equals(l.getAtom().getName())){
							semiPositive = true;
						}
					}
				}
			}

		}

		return semiPositive;
	}

	public StratifiedDatalogProgram stratify(){

		HashMap<String, Integer> stratum = new HashMap<String, Integer>();
		Set<String> predicates = getAllPredicates();

		for(String p : predicates){
			stratum.put(p, 0);
		}

		boolean change = true;

		while (change){

			change = false;
			
			for(Tgd t : mapping.getTgds()){
				String p = t.getRight().getName();

				for(Literal l : t.getLeft()){
					if(!l.getFlag()){
						int strat = Math.max(stratum.get(p), stratum.get(l.getAtom().getName()) + 1);
						if(stratum.get(p) != strat)change = true;
						stratum.put(p, strat);
					}
					else{
						int strat = Math.max(stratum.get(p), stratum.get(l.getAtom().getName()));
						if(stratum.get(p) != strat)change = true;
						stratum.put(p, strat);

					}
				}
			}

		}


		StratifiedDatalogProgram sProgram = new StratifiedDatalogProgram(mapping);

		for(Relation r : mapping.getEDB()){
			sProgram.add(stratum.get(r.getName()), r);
		}

		for(AbstractRelation r: mapping.getIDB()){
			sProgram.add(stratum.get(r.getName()), r);
		}

		for(Tgd t : mapping.getTgds()){
			sProgram.add(stratum.get(t.getRight().getName()), t);
		}



		return sProgram;

	}

	private Set<String> getAllPredicates(){

		Set<String> set = new HashSet<String>();

		for(Relation r : mapping.getEDB()){
			set.add(r.getName());
		}
		for(AbstractRelation r : mapping.getIDB()){
			set.add(r.getName());
		}

		for(Tgd t : mapping.getTgds()){

			set.add(t.getRight().getName());
			for(Literal l : t.getLeft()){
				set.add(l.getAtom().getName());

			}
		}

		return set;

	}


	private DirectedGraph<String, SignedEdge> generateMappingGraph(){

		DirectedGraph<String, SignedEdge> g =
				new DefaultDirectedGraph<String, SignedEdge>(SignedEdge.class);

		for(Tgd t : mapping.getTgds()){

			t.getRight().getName();
			if (!g.containsVertex(t.getRight().getName())){
				g.addVertex(t.getRight().getName());
			}

			for(Literal l : t.getLeft()){

				String exprName = l.getAtom().getName();
				if (!g.containsVertex(exprName)){
					g.addVertex(exprName);
				}

				SignedEdge edge = new SignedEdge(l.getFlag());

				g.addEdge(exprName, t.getRight().getName(), edge);

			}
		}

		return g;

	}

	public Mapping getMapping(){
		return mapping;
	}


	private class SignedEdge{
		private boolean sign;

		public SignedEdge(boolean sign){
			this.sign = sign;
		}

		public boolean getSign(){
			return sign;
		}
	}

}
