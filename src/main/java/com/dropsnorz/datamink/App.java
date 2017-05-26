package com.dropsnorz.datamink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.springframework.shell.Bootstrap;

import com.dropsnorz.datamink.core.DatalogEngine;

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

		if(args.length == 1){
			
			try {
				FileInputStream fis = new FileInputStream(args[0]);
				MappingParser parser = new MappingParser(fis);
				Mapping mapping = parser.mapping();
				
				DatalogEngine engine = new DatalogEngine(mapping);
				
				System.out.println("===Program Type===");
				System.out.println(engine.getProgramType());
				System.out.println("===Stratification===");
				System.out.println(engine.stratify());
				System.out.println("===Evaluation===");
				System.out.println(engine.eval());
				System.out.println("===SQL===");
				System.out.println(engine.toSql());
				
			} catch (FileNotFoundException e) {
				
				// TODO Auto-generated catch block
				System.out.println("Error: Invalid file");
			}
			

		}
		else{


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
}
