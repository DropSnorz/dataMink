package com.dropsnorz.datamink.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.dropsnorz.datamink.service.FileService;

@Component
public class FileCommands implements CommandMarker {

	@Autowired
	FileService fileService;
	
	@CliCommand(value = "dm load", help = "Load Datalog file")
	public String simple(
			@CliOption(key = { "" }, mandatory = false, help = "File to load in DataMink") final String defaultPath,
			@CliOption(key = { "file" }, mandatory = false, help = "File to load in DataMink") final String path){
		
		
		if(defaultPath != null && fileService.loadFile(defaultPath)){
			return "File successfuly loaded: " + fileService.getCurrentFilePath();

		}
		else if (path != null && fileService.loadFile(path)){
			return "File successfuly loaded: " + fileService.getCurrentFilePath();

		}
		
		else{
			return "Invalid file path";
		}
}
	

}
