package com.dropsnorz.datamink.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HistoryFileNameProvider extends DefaultHistoryFileNameProvider {
	
	@Override
	public String getHistoryFileName(){
		return "datamink.log";
		
	}
	
	@Override
	public String getProviderName(){
		return "DataMink History Provider";
	}

}
