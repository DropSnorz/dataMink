package com.dropsnorz.datamink.commands;

import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomPromptProvider extends DefaultPromptProvider {

	@Override
	public String getPrompt() {
		return "dm-shell>";
	}

	
	@Override
	public String getProviderName() {
		return "DataMink prompt provider";
	}
}

