package com.dropsnorz.datamink.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.dropsnorz.datamink.service.FileService;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;

@Component
public class ParserCommands implements CommandMarker {
	
	@Autowired
	FileService fileService;
	
	@CliCommand(value = "dm parse", help = "Print a simple hello world message")
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
				return "File successfully parsed";
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
