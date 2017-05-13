package com.dropsnorz.datamink.commands;

import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BannerProvider extends DefaultBannerProvider  {

	public String getBanner() {
		StringBuffer buf = new StringBuffer();
	    buf.append("|***********************************************************************************|" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                                                                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                                               _,-/\\^---,          |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                                             _/;; ~~  {0 `---v     |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                     ;;\"~~~~~~~~\";~~\\      _/   ;;     ~ _../      |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|          DataMink                 ;\" ;;    ;;;      \\___/::    ;;,'~~~~           |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|        By DropSnorz             /;   ;;;______;;;;  ;;;    ::,/                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                               /;;V_;; _-~~~~~~~~~~;_  ;;;   ,/                    |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                              | :/ / ,/              \\_  ~~)/                      |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|     Based on mif37-dex       |:| / /~~~=              \\;; \\~~=                    |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                              ;:;{::~~~  ~=              \\__~~~=                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                           ;~~:;  ~~~~~~                  ~~~~~~                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                           \\/~~                                                    |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                                                                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|                                                                                   |" + OsUtils.LINE_SEPARATOR);
	    buf.append("|***********************************************************************************|" + OsUtils.LINE_SEPARATOR);
		buf.append("Version:" + this.getVersion());
		return buf.toString();
	}

	public String getVersion() {
		return "0.0.1";
	}

	public String getWelcomeMessage() {
		return "Welcome to DataMink CLI";
	}
	
	@Override
	public String getProviderName() {
		return "DataMink banner";
	}
}