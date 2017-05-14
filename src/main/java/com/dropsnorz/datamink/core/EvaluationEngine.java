package com.dropsnorz.datamink.core;

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
		
		for(Tgd t : mapping.getTgds()){
			for(Literal l : t.getLeft()){
				if(!l.getFlag()){
					positive = false;
				}
			}
			
		}
		
		if(positive){
			return ProgramType.POSITIVE;

		}
		else{
			return ProgramType.UNKNOW;
		}
	}
	
	

}
