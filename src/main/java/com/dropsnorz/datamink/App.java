package com.dropsnorz.datamink;

import java.io.IOException;

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
    	
    	//BasicConfigurator.configure();
        
        try {
			Bootstrap.main(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }
}
