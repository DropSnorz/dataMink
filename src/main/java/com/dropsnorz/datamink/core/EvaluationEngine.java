package com.dropsnorz.datamink.core;

import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class EvaluationEngine {
	
	
	public EvaluationEngine(){
		
		
	}
	
	public ProgramType computeProgramType(Mapping mapping){
		
		//Check for positive datalog program
		boolean positive = true;
		boolean stratifiable = true;
		
		for(Tgd t : mapping.getTgds()){
			for(Literal l : t.getLeft()){
				if(!l.getFlag()){
					positive = false;
				}
			}
			
		}
		
		if(! positive){
			
			DirectedGraph<String, SignedEdge> graph = generateMappingGraph(mapping);
			
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
			//check cycles
			
			
			
		}
		
		if(positive){
			return ProgramType.POSITIVE;

		}
		else if (stratifiable){
			return ProgramType.SEMI_POSITIVE;
		}
		else{
			return ProgramType.UNKNOW;
		}
	}
	
	
	private DirectedGraph<String, SignedEdge> generateMappingGraph(Mapping mapping){
		
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
