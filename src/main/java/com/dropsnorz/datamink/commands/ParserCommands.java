package com.dropsnorz.datamink.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import com.dropsnorz.datamink.core.EvaluationEngine;
import com.dropsnorz.datamink.core.ProgramEvaluator;
import com.dropsnorz.datamink.core.ProgramType;
import com.dropsnorz.datamink.core.SQLTranslator;
import com.dropsnorz.datamink.core.StratifiedDatalogProgram;
import com.dropsnorz.datamink.service.FileService;
import com.dropsnorz.datamink.utils.MappingStringUtils;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;

@Component
public class ParserCommands implements CommandMarker {

	@Autowired
	FileService fileService;

	@CliCommand(value = "dm parse", help = "Parse the given datalog program and show results")
	public String parse(
			@CliOption(key = { "" }, mandatory = false, help = "Datalog file to parse") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "Datalog file to parse") final String path){


		try {
			FileInputStream fis;

			if(defaultPath != null){
				fis = fileService.getStreamFromPathOrLocal(defaultPath);
			}
			else{
				fis = fileService.getStreamFromPathOrLocal(path);
			}
			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				return "File successfully parsed !" + OsUtils.LINE_SEPARATOR + MappingStringUtils.generateString(mapping);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}


	@CliCommand(value = "dm prgm-type", help = "Returns datalog program type (Positive, Semo-positive, Stratified, Unknow)")
	public String prgmType(
			@CliOption(key = { "" }, mandatory = false, help = "Datalog file to analyse") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "Datalog file to analyse") final String path){

		try {
			FileInputStream fis;

			if(defaultPath != null){
				fis = fileService.getStreamFromPathOrLocal(defaultPath);
			}
			else{
				fis = fileService.getStreamFromPathOrLocal(path);
			}

			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				EvaluationEngine engine = new EvaluationEngine(mapping);
				ProgramType type = engine.computeProgramType();

				return "Program is " + type;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}


	@CliCommand(value = "dm stratify", help = "Returns datalog program type (Positive, Semo-positive, Unknow)")
	public String stratify(
			@CliOption(key = { "" }, mandatory = false, help = "Datalog program file to stratify") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "Datalog program file to stratify") final String path){

		try {
			FileInputStream fis;

			if(defaultPath != null){
				fis = fileService.getStreamFromPathOrLocal(defaultPath);
			}
			else{
				fis = fileService.getStreamFromPathOrLocal(path);
			}


			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				EvaluationEngine engine = new EvaluationEngine(mapping);
				ProgramType type = engine.computeProgramType();


				if(type == ProgramType.POSITIVE){
					return "Program is Positive, no need to stratify it";
				}
				else if(type == ProgramType.SEMI_POSITIVE || type == ProgramType.STRATIFIABLE){
					return engine.stratify().toString();
				}
				else{
					return "Program is not Stratifiable";

				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}



	@CliCommand(value = "dm eval", help = "Returns datalog program type (Positive, Semo-positive, Unknow)")
	public String eval(
			@CliOption(key = { "" }, mandatory = false, help = "Datalog program file to evaluate") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "Datalog program file to evaluate") final String path){

		try {
			FileInputStream fis;

			if(defaultPath != null){
				fis = fileService.getStreamFromPathOrLocal(defaultPath);
			}
			else{
				fis = fileService.getStreamFromPathOrLocal(path);
			}

			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				EvaluationEngine engine = new EvaluationEngine(mapping);

				ProgramType type = engine.computeProgramType();
				ProgramEvaluator evaluator = new ProgramEvaluator(mapping);

				if(type == ProgramType.POSITIVE){
					evaluator.evaluation();
				}
				else if (type == ProgramType.SEMI_POSITIVE || type == ProgramType.STRATIFIABLE){
					StratifiedDatalogProgram stratification = engine.stratify();
					evaluator.stratifiedEvaluation(stratification);
				}
				else{
					return "Error: could not compute program type";
				}

				return evaluator.getComputedFactsDisplay();


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}
	
	
	
	@CliCommand(value = "dm toSql", help = "Returns datalog program type (Positive, Semo-positive, Unknow)")
	public String toSql(
			@CliOption(key = { "" }, mandatory = false, help = "Datalog program file to evaluate") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "Datalog program file to evaluate") final String path){

		try {
			FileInputStream fis;

			if(defaultPath != null){
				fis = fileService.getStreamFromPathOrLocal(defaultPath);
			}
			else{
				fis = fileService.getStreamFromPathOrLocal(path);
			}

			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				EvaluationEngine engine = new EvaluationEngine(mapping);

				ProgramType type = engine.computeProgramType();
				ProgramEvaluator evaluator = new ProgramEvaluator(mapping);

				if(type == ProgramType.POSITIVE){
					evaluator.evaluation();
				}
				else if (type == ProgramType.SEMI_POSITIVE || type == ProgramType.STRATIFIABLE){
					StratifiedDatalogProgram stratification = engine.stratify();
					evaluator.stratifiedEvaluation(stratification);
				}
				else{
					return "Error: could not compute program type";
				}
				
				SQLTranslator translator = new SQLTranslator(mapping);

				return translator.toSql();


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}


}
