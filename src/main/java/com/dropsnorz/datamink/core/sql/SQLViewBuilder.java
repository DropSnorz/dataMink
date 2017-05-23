package com.dropsnorz.datamink.core.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class SQLViewBuilder {

	String viewName;
	ArrayList<String> selectFields;

	HashMap<Literal, TableEntity> tables = new HashMap<Literal, TableEntity>();
	ArrayList<TableReference> references = new ArrayList<TableReference>();


	public SQLViewBuilder(String viewName){

		this.viewName = viewName;
		this.selectFields = new ArrayList<String>();
	}
	
	public SQLViewBuilder asSelect(ArrayList<String> fields){
		
		this.selectFields = fields;
		
		return this;
	}

	public SQLViewBuilder bindLiterals(Collection<Literal> literals){
		tables = new HashMap<Literal, TableEntity>();
		
		HashMap<String, Integer> tablePrefix = new HashMap<String, Integer>();

		for(Literal l : literals){

			String prefix = "";

			if(!tablePrefix.containsKey(l.getAtom().getName())){
				prefix = l.getAtom().getName() + "1";
				tablePrefix.put(l.getAtom().getName(), 1);
			}
			else{
				int prefixCount = tablePrefix.get(l.getAtom().getName()) + 1;
				prefix = l.getAtom().getName() + Integer.toString(prefixCount);
			}

			TableEntity table = new TableEntity(l.getAtom().getName());
			table.setPrefix(prefix);

			ArrayList<String> variables = new ArrayList<String>();
			for(Variable v : l.getAtom().getVars()){
				variables.add(v.getName());
			}
			table.setColumnsMap(variables);
			
			ArrayList<String> columns = new ArrayList<String>();
 			for(int k = 0; k < variables.size(); k ++){
				columns.add(Integer.toString(k + 1));
			}
			table.setColumns(columns);
			
			tables.put(l, table);
		}
		
		for(TableEntity table : tables.values()){
			System.out.println(table);
		}
		

		if(tables.values().size() > 1){
			for(TableEntity table : tables.values()){

				if(!table.isFullyLinked()){

					for(String column : table.getUnlinkedColumns()){

						String mapping = table.getColumnMapByColumnName(column);

						boolean linked = false;
						for(TableEntity table2 : tables.values()){

							if(!linked && !table2.equals(table)){

								if(table2.getColumnsMap().contains(mapping)){

									TableReference reference = new TableReference(table, column, table2, table2.getColumnNameByColumnMap(mapping));
									references.add(reference);
									linked = true;
								}
							}
						}

					}

				}

			}

		}

		return this;
	}

	public String build(){

		String query = "CREATE OR REAPLACE VIEW " + viewName + " AS SELECT ";

		int i = 0;
		for(String selectField : selectFields){
			boolean selected = false;
			for(TableEntity table : tables.values()){
				
				if(!selected && table.getColumnsMap().contains(selectField)){
					
					query += table.getPrefix() + "." + table.getColumnNameByColumnMap(selectField);
					selected = true;
				}	
			}
			
			if(i < selectFields.size()- 1){
				query += ", ";
			}
			
			i++;
		}
		
		//DO SELECT STUFF

		query += " FROM ";

		i = 0;
		for(TableEntity table : tables.values()){

			query += table.getName() + " AS " + table.getPrefix();

			if(i < tables.values().size() - 1){
				query += ", ";
			}
			i++;

		}

		if(references.size() > 0 ){


			String whereQuery = " WHERE ";

			i=0;
			for(TableReference relation : references){
				whereQuery += relation.getInReference().getPrefix() + "." 
						+ relation.getInColumn() + " = "
						+ relation.getOutReference().getPrefix() + "." + relation.getOutColumn();
				if(i < references.size() - 1){
					whereQuery += " AND ";
				}
				
				i++;
				
			}
			
			query += whereQuery;
			
		}

		query +=";\n";

		return query;

	}


	private boolean isTablesColumnsFullyReferenced(){

		for(TableEntity table : tables.values()){

			if(!table.isFullyLinked()){
				return false;
			}
		}

		return true;
	}


}
