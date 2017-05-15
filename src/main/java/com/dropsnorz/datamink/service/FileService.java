package com.dropsnorz.datamink.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
	
	public FileInputStream getStreamFromPathOrLocal(String path) throws FileNotFoundException{
		FileInputStream fis = null;
		if(path != null){
			fis = new FileInputStream(path);
		}
		
		else if(isCurrentFileLoaded()){
			fis = new FileInputStream(currentFilePath);
		}
		
		else{
			throw new FileNotFoundException();
		}
		return fis;
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
