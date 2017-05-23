package com.dropsnorz.datamink.utils;

import org.springframework.util.StringUtils;

import fr.univlyon1.mif37.dex.mapping.AbstractArgument;
import fr.univlyon1.mif37.dex.mapping.AbstractRelation;
import fr.univlyon1.mif37.dex.mapping.Atom;
import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Value;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class MappingStringUtils {
	
	public static String generateString(Mapping mapping){
		
		String output = "";
		
		output += "--EDB (" + mapping.getEDB().size() + "): \n";
		for(Relation r : mapping.getEDB()){
			output += "\t " + generateRelationString(r) + "\n";
			
		}
		
		output += "--IDB (" + mapping.getIDB().size() + "): \n";
		for(AbstractRelation r : mapping.getIDB()){
			output += "\t " + generateAbstractRelationString(r) + "\n";
		}
		
		output += "--Tgd (" + mapping.getTgds().size() + "): \n";
		for(Tgd t : mapping.getTgds()){
			output += "\t " + generateTgdString(t) + "\n";
		}
		
		return output;
	}
	
	public static String generateRelationString(Relation relation){
		return relation.getName() + "(" + StringUtils.arrayToCommaDelimitedString(relation.getAttributes()) + ")";
		
	}
	
	public static String generateAbstractRelationString(AbstractRelation relation){
		return relation.getName() + "(" + generateAbstractArgumentsListString(relation.getAttributes()) + ")";
		
	}
	
	public static String generateTgdString(Tgd t){
		String output = "";
		int i = 0;
		for(Literal l : t.getLeft() ){
			output += generateLiteralString(l);
			if(i < t.getLeft().size() - 1){
				output += ",";
			}
			i++;
		}
		
		output += " -> " + generateAtomString(t.getRight());
		
		
		
		return output;
	}
	
	public static String generateLiteralString(Literal l){
		
		String output = "";
		if(!l.getFlag()){
			output += "(NEG ";
		}
		
		output += generateAtomString(l.getAtom());
		
		if(!l.getFlag()){
			output += ")";
		}
		
		return output;
	}
	
	public static String generateAtomString(Atom a){
		String output = "";
		output += a.getName();		
		output +="(";
				
		int i = 0;
		for(Variable v : a.getVars()){
			output += v.getName();
			
			if(i < a.getVars().size() - 1){
				output +=",";
			}
			i++;
		}
		
		output += ")";
		
		return output;
		
	}
	
	public static String generateAbstractArgumentString(AbstractArgument a){
		
		if(a.isAttribute()){
			return a.getAtt();
		}
		else{
			return a.getVar().getName();
		}
	}
	
	private static String generateAbstractArgumentsListString(AbstractArgument[] array){
		
		String output = "";
		
		for(int i =0; i< array.length; i++){
			output += generateAbstractArgumentString(array[i]);
			if(i < array.length - 1){
				output += ",";
			}
		}
		
		return output;
	}

}
