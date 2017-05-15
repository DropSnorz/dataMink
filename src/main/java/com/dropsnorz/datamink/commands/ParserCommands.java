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
import com.dropsnorz.datamink.core.ProgramType;
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
			@CliOption(key = { "file" }, mandatory = false, help = "The hello world message") final String path){


		try {
			FileInputStream fis = fileService.getStreamFromPathOrLocal(path);
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
	
	
	@CliCommand(value = "dm prgm-type", help = "Returns datalog program type (Positive, Semo-positive, Unknow)")
	public String prgmType(
			@CliOption(key = { "file" }, mandatory = false, help = "The hello world message") final String path){

		try {
			FileInputStream fis = fileService.getStreamFromPathOrLocal(path);

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
			@CliOption(key = { "file" }, mandatory = false, help = "The hello world message") final String path){

		try {
			FileInputStream fis = fileService.getStreamFromPathOrLocal(path);

			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				EvaluationEngine engine = new EvaluationEngine(mapping);
				ProgramType type = engine.computeProgramType();

				
				if(type == ProgramType.POSITIVE){
					return "Program is Positive, no need to stratify it";
				}
				else if(type == ProgramType.SEMI_POSITIVE){
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


}
