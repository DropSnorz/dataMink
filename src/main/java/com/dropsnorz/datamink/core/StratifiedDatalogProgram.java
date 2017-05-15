package com.dropsnorz.datamink.core;

import java.util.ArrayList;

import com.dropsnorz.datamink.utils.MappingStringUtils;

import fr.univlyon1.mif37.dex.mapping.AbstractRelation;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;

public class StratifiedDatalogProgram {

	ArrayList<ArrayList<Object>> partitions;

	StratifiedDatalogProgram(){

		partitions = new ArrayList<ArrayList<Object>>();
	}

	public void add(int partition, Object object){

		while(partition + 1 > partitions.size()){
			partitions.add(new ArrayList<Object>());
		}

		partitions.get(partition).add(object);
	}


	public String toString(){

		String output = "";
		int i =0;
		for(ArrayList<Object> partition: partitions){

			output += "-- P" + i + "\n";

			for(Object element : partition){
				if(element instanceof Tgd){
					output += MappingStringUtils.generateTgdString((Tgd) element);
				}
				else if (element instanceof AbstractRelation){
					output += MappingStringUtils.generateAbstractRelationString((AbstractRelation)element);
				}
				else if (element instanceof Relation){
					output += MappingStringUtils.generateRelationString((Relation)element);
				}
				else{
					output += element.toString();
				}

				output += "\n";
			}

			i = i +1;
		}

		return output;

	}




}
