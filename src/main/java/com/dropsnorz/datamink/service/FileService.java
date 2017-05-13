package com.dropsnorz.datamink.service;

import java.io.File;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class FileService {
	
	public String currentFilePath;
	
	public FileService(){
		
	}
	
	public String getFileContent(){
		return null;
		
	}
	
	public boolean loadFile(String path){
		
		File file = new File(path);
		boolean exists =  file.exists();
		
		if(exists){
			currentFilePath = path;
		}
		else{
			currentFilePath = null;
		}
		
		return exists;
	}
	
	public boolean isCurrentFileLoaded(){
		return currentFilePath != null;
	}
	
	public String getCurrentFilePath(){
		return currentFilePath;
	}

}
