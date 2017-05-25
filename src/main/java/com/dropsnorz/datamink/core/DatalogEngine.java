package com.dropsnorz.datamink.core;

import fr.univlyon1.mif37.dex.mapping.Mapping;

public class DatalogEngine {

	protected DatalogProgram datalogProgram; 

	public DatalogEngine(Mapping mapping){

		this.datalogProgram = new DatalogProgram(mapping);
	}
	public DatalogEngine(DatalogProgram program){
		this.datalogProgram = program;
	}
	
	public String getProgramType(){
		return datalogProgram.getProgramType().toString();
	}
	
	public String stratify(){
		
		ProgramType type = datalogProgram.getProgramType();


		if(type == ProgramType.POSITIVE){
			return "Program is Positive, no need to stratify it";
		}
		else if(type == ProgramType.SEMI_POSITIVE || type == ProgramType.STRATIFIABLE){
			return datalogProgram.stratify().toString();
		}
		else{
			return "Program is not Stratifiable";

		}

	}
	
	public String eval(){
		
		
		ProgramType type = datalogProgram.getProgramType();
		ProgramEvaluator evaluator = new ProgramEvaluator(datalogProgram);

		if(type == ProgramType.POSITIVE){
			evaluator.evaluation();
		}
		else if (type == ProgramType.SEMI_POSITIVE || type == ProgramType.STRATIFIABLE){
			StratifiedDatalogProgram stratification = datalogProgram.stratify();
			evaluator.stratifiedEvaluation(stratification);
		}
		else{
			return "Error: could not compute program type";
		}

		return evaluator.getComputedFactsDisplay();

	}
	
	public String toSql() {

		SQLTranslator translator = new SQLTranslator(datalogProgram.getMapping());

		return translator.toSql();
	}
	
	

}
