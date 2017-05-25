package com.dropsnorz.datamink.core.sql;

public class TableReference {
	
	TableEntity inReference;
	String inColumn;
	
	TableEntity outReference;
	String outColumn;
	
	public TableReference(TableEntity inReference, String inColumn, TableEntity outReference, String outColumn) {
		super();
		this.inReference = inReference;
		this.inColumn = inColumn;
		this.outReference = outReference;
		this.outColumn = outColumn;
		
		inReference.getOutputReferences().add(this);
		outReference.getInputReferences().add(this);
		
	}

	public TableEntity getInReference() {
		return inReference;
	}

	public void setInReference(TableEntity inReference) {
		this.inReference = inReference;
	}

	public String getInColumn() {
		return inColumn;
	}

	public void setInColumn(String inColumn) {
		this.inColumn = inColumn;
	}

	public TableEntity getOutReference() {
		return outReference;
	}

	public void setOutReference(TableEntity outReference) {
		this.outReference = outReference;
	}

	public String getOutColumn() {
		return outColumn;
	}

	public void setOutColumn(String outColumn) {
		this.outColumn = outColumn;
	}
	
	
	
	
	

}
