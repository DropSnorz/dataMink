package com.dropsnorz.datamink.core.sql;

import org.springframework.util.StringUtils;

public class SQLInsertBuilder {
	
	String tableName;
	String[] valuesArray;
	
	SQLInsertBuilder(String tableName){
		this.tableName = tableName;
	}
	
	public SQLInsertBuilder insertInto(String tableName){
		this.tableName = tableName;
		return this;
	}
	
	public SQLInsertBuilder values(String[] valuesArray){
		this.valuesArray = valuesArray.clone();
		
		for(int i = 0; i < this.valuesArray.length; i ++){
			this.valuesArray[i] = "'" + valuesArray[i] + "'";
		}
		
		
		
		return this;
	}
	
	public String build(){
		return "INSERT INTO " + tableName + " VALUES (" + StringUtils.arrayToCommaDelimitedString(valuesArray) + ");";
	}

}
