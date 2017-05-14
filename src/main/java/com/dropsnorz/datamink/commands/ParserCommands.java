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

		FileInputStream fis;

		try {
			if(path != null){
				fis = new FileInputStream(path);
			}
			else{
				if(fileService.isCurrentFileLoaded()){
					fis = new FileInputStream(fileService.getCurrentFilePath());
				}
				else{
					return "Error: No file loaded";
				}
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
	
	
	
	
	@CliCommand(value = "dm prgm-type", help = "Print a simple hello world message")
	public String prgmType(
			@CliOption(key = { "file" }, mandatory = false, help = "The hello world message") final String path){

		FileInputStream fis;

		try {
			if(path != null){
				fis = new FileInputStream(path);
			}
			else{
				if(fileService.isCurrentFileLoaded()){
					fis = new FileInputStream(fileService.getCurrentFilePath());
				}
				else{
					return "Error: No file loaded";
				}
			}

			MappingParser mp = new MappingParser(fis);
			try {
				Mapping mapping = mp.mapping();
				
				EvaluationEngine engine = new EvaluationEngine();
				ProgramType type = engine.computeProgramType(mapping);
				
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


}
