package com.dropsnorz.datamink.core;

public enum ProgramType {
	POSITIVE ("Positive"),
	STRATIFIABLE ("Stratifiable"),
	SEMI_POSITIVE ("Semi-Positive"),
	UNKNOW ("Unknow");
	
	
    private final String text;       

    private ProgramType(String s) {
        text = s;
    }
    
    public String toString(){
    	return this.text;
    }

}
