package com.dropsnorz.datamink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.springframework.shell.Bootstrap;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException
    {
    	
    	
        
        try {
        	
        	ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
            argsList.add("--disableInternalCommands");
            String[] argsArray = new String[argsList.size()];
            argsArray = argsList.toArray(argsArray);
            
			Bootstrap.main(argsArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }
}
