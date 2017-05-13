package com.dropsnorz.datamink;

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
        System.out.println( "Hello World!" );
        
        
        MappingParser mp = new MappingParser(System.in);
        Mapping mapping = mp.mapping();
        
        System.out.println(mapping.getEDB().size());
        

    }
}
