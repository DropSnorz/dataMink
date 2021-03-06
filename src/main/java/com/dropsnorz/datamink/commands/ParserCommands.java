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

import com.dropsnorz.datamink.core.DatalogEngine;
import com.dropsnorz.datamink.core.DatalogProgram;
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

	@CliCommand(value = "dm parse", help = "Compute program type for the given or pre-loaded datalog file and prints the result")
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
				DatalogEngine engine = new DatalogEngine(mapping);

				return "Program is " + engine.getProgramType();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}


	@CliCommand(value = "dm stratify", help = "Stratify the given or pre-loaded datalog file and prints all stratums")
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
				DatalogEngine engine = new DatalogEngine(mapping);
				
				return engine.stratify();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}



	@CliCommand(value = "dm eval", help = "Compute evaluation for the given or pre-loaded datalog file and prints inferred facts")
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
				DatalogEngine engine = new DatalogEngine(mapping);

				return engine.eval();


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			return "File not found !";
		}

		return null;

	}
	
	
	
	@CliCommand(value = "dm tosql", help = "Translate the given or pre-loaded datalog file to SQL and prints the result")
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
				DatalogEngine engine = new DatalogEngine(mapping);

				return engine.toSql();


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
