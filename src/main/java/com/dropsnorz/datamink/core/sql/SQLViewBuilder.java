package com.dropsnorz.datamink.core.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.util.StringUtils;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Variable;

public class SQLViewBuilder {

	protected String viewName;
	protected ArrayList<String> selectFields;

	protected HashMap<Literal, TableEntity> tables = new HashMap<Literal, TableEntity>();
	protected ArrayList<TableReference> references = new ArrayList<TableReference>();

	protected ArrayList<Literal> negatedLiterals;


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
		negatedLiterals = new ArrayList<Literal>();

		HashMap<String, Integer> tablePrefix = new HashMap<String, Integer>();

		for(Literal l : literals){

			if(!l.getFlag()){
				negatedLiterals.add(l);
			}
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

			if(negatedLiterals.size() == 0){

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
			}
			
			//Generate clauses with negated literals
			else{
				
				ArrayList<String>  subWhereClauses = new ArrayList<String>();
				
				for(Literal l : negatedLiterals){
					String literalName = l.getAtom().getName();
					for(TableReference reference : references){
						
						String subWhereClause = "(";
						
						subWhereClause += generateRelationString(reference, "<>");
						
						if(reference.getInReference().getName().equals(literalName)
								||reference.getOutReference().getName().equals(literalName)){
							
							for(TableReference printReference : references){
								
								if(!printReference.equals(reference)){
									subWhereClause += " AND " + generateRelationString(printReference, "=");
									//String assume ==
								}
							}
						}
						
						subWhereClause += ")";
						
						subWhereClauses.add(subWhereClause);
						
					}
				}
				
				
				whereQuery = StringUtils.collectionToDelimitedString(subWhereClauses, " OR ");
				
				ArrayList<String> positiveClauses = new ArrayList<String>();
				
				for(TableReference reference : references){
					
					boolean addReference = true;
					
					for(Literal l : negatedLiterals){
						
						if(reference.getInReference().getName().equals(l.getAtom().getName())
								|| reference.getOutReference().getName().equals(l.getAtom().getName())){
							addReference = false;
						}
					}
					
					if(addReference){
						positiveClauses.add(generateRelationString(reference, "="));
					}
			
				}
				
				if(positiveClauses.size() > 0){
					whereQuery = StringUtils.collectionToDelimitedString(positiveClauses, " AND ") + " AND " + "(" + whereQuery + ")";
				}
				
				whereQuery = " WHERE " + whereQuery;
				
			}

			query += whereQuery;

		}

		query +=";\n";

		return query;

	}
	
	private String generateRelationString(TableReference relation, String operator){
		
		String output = relation.getInReference().getPrefix() + "." 
				+ relation.getInColumn() + "  " + operator + " "
				+ relation.getOutReference().getPrefix() + "." + relation.getOutColumn();
		
		return output;
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
