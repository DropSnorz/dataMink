package com.dropsnorz.datamink.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.dropsnorz.datamink.core.sql.SQLBuilder;
import com.dropsnorz.datamink.core.sql.TableEntity;
import com.dropsnorz.datamink.utils.MappingStringUtils;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Relation;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class SQLTranslator {

	Mapping mapping;
	public SQLTranslator(Mapping mapping){
		this.mapping = mapping;
	}

	public String toSql(){

		String queries =  generateCreateAndInsertQueries();

		for(Tgd t : mapping.getTgds()){

			if(isRecursive(t)){
				queries += generateComments("Recursive datalog rule not translated: "
						+ MappingStringUtils.generateTgdString(t));
			}
			else{
				queries += generateNonRecursiveView(t);
			}
		}
		return queries;
	}

	private String generateCreateAndInsertQueries(){

		HashMap<String, Integer> tables = new HashMap<String, Integer>();
		String queries = "";

		for(Relation r: mapping.getEDB()){

			tables.put(r.getName(), r.getAttributes().length);

			queries += ("INSERT INTO " + r.getName() + " VALUES (" + StringUtils.arrayToCommaDelimitedString(r.getAttributes()) + "); \n");
		}

		for(String table : tables.keySet()){

			int nColumns = tables.get(table);
			String[] columns = new String[nColumns];

			for(int i = 0; i < nColumns; i++){
				columns[i] = table + Integer.toString(i) + " VARCHAR(150)";
			}
			String tableQuery = "CREATE TABLE "+ table + "( " + StringUtils.arrayToCommaDelimitedString(columns) + " )";

			queries = tableQuery + "\n" + queries;
		}

		return queries;
	}

	private String generateNonRecursiveView(Tgd t){

		ArrayList<String> fields = new ArrayList<String>();
		for(Variable v : t.getRight().getVars()){
			fields.add(v.getName());
		}
		String query = new SQLBuilder(t.getRight().getName()).asSelect(fields).bindLiterals(t.getLeft()).build();
		
		return query;
	}
	private String generateComments(String text){

		return "/* " + text + " */ \n";
	}

	private boolean isRecursive(Tgd t){

		for(Literal l : t.getLeft()){
			if(l.getAtom().getName().equals(t.getRight().getName())){
				return true;
			}
		}
		return false;
	}

}
