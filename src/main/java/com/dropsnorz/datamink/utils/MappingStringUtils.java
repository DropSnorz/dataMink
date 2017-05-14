package com.dropsnorz.datamink.utils;

import org.springframework.util.StringUtils;

import fr.univlyon1.mif37.dex.mapping.AbstractRelation;
import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;

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
		return relation.getName() + "(" + StringUtils.arrayToCommaDelimitedString(relation.getAttributes()) + ")";
		
	}
	
	public static String generateTgdString(Tgd t){
		String output = "";
		for(Literal l : t.getLeft() ){
			output += generateLiteralString(l);
		}
		
		output += " -> " + t.getRight().getName();
		
		return output;
	}
	
	public static String generateLiteralString(Literal l){
		
		String output = "";
		if(!l.getFlag()){
			output += "(NEG ";
		}
		
		output += l.getAtom().getName();
		
		if(!l.getFlag()){
			output += ")";
		}
		
		return output;
	}

}
