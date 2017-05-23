package com.dropsnorz.datamink.core.sql;

public class SQLBuilder {
	
	public SQLBuilder(){
		
	}
	
	public SQLTableBuilder createTable(String tableName){
		return new SQLTableBuilder(tableName);
	}
	public SQLInsertBuilder insertInto(String tableName){
		return new SQLInsertBuilder(tableName);
	}
	
	public SQLViewBuilder createView(String viewName){
		return new SQLViewBuilder(viewName);
	}
	
	public String comments(String text){
		return "/* " + text + " */ \n";
	}

}
