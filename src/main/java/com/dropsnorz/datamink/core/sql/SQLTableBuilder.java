package com.dropsnorz.datamink.core.sql;

import java.util.ArrayList;

import org.springframework.util.StringUtils;

public class SQLTableBuilder {
	
	protected String tableName;
	protected ArrayList<String> columns;
	
	public SQLTableBuilder(String tableName){
		this.tableName = tableName;
		this.columns = new ArrayList<String>();
	}
	
	public SQLTableBuilder appendColumns(String name, String type){
		
		columns.add(name + " " + type);
		return this;
	}
	
	public String build(){
		
		String[] columnsArray = new String[columns.size()];
		columns.toArray(columnsArray);
		
		return "CREATE TABLE "+ tableName + " ( " + StringUtils.arrayToCommaDelimitedString(columnsArray) + " );";

	}
	
	

}
