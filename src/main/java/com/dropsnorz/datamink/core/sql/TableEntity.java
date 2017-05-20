package com.dropsnorz.datamink.core.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableEntity {
	String name;
	ArrayList<String> columnsMap;
	ArrayList<String> columns;

	ArrayList<TableReference> inputReferences;
	ArrayList<TableReference> outputReferences;

	String prefix;


	public TableEntity(String name){
		this.name = name;

		columns = new ArrayList<String>();
		inputReferences = new ArrayList<TableReference>();
		outputReferences = new ArrayList<TableReference>();
	}

	public String getColumnMapByColumnName(String name){

		return columnsMap.get((columns.indexOf(name)));

	}

	public String getColumnNameByColumnMap(String map){

		return columns.get((columnsMap.indexOf(map)));

	}

	public boolean isFullyLinked(){

		Set<String> linkedColumns = new HashSet<String>();

		for(TableReference r : inputReferences){

			for(String column : columns){

				if(r.getOutColumn().equals(column)){

					linkedColumns.add(column);
				}
			}
		}

		for(TableReference r : outputReferences){

			for(String column : columns){

				if(r.getInColumn().equals(column)){

					linkedColumns.add(column);
				}
			}
		}

		return columns.size() == linkedColumns.size();
	}

	public ArrayList<String> getUnlinkedColumns(){

		ArrayList<String> unlinkedColumns = new ArrayList<String>(columns);

		for(TableReference r : inputReferences){

			for(String column : columns){

				if(r.getOutColumn().equals(column)){

					unlinkedColumns.remove(column);
				}
			}
		}

		for(TableReference r : outputReferences){

			for(String column : columns){

				if(r.getInColumn().equals(column)){

					unlinkedColumns.remove(column);
				}
			}
		}

		return unlinkedColumns;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;

	}
	public ArrayList<String> getColumnsMap() {
		return columnsMap;
	}

	public String[] getColumnsMapArray(){

		String[] tab = new String[columnsMap.size()];
		columnsMap.toArray(tab);
		return tab;
	}
	public void setColumnsMap(ArrayList<String> columns) {
		this.columnsMap = columns;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ArrayList<String> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}

	public ArrayList<TableReference> getInputReferences() {
		return inputReferences;
	}

	public ArrayList<TableReference> getOutputReferences() {
		return outputReferences;
	}

	
	public String toString(){
		return "TABLE:" + name;
	}



}
