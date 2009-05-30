package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.wiring.WiringService;

public class StandardSevletContainer extends WebContainer{

	public StandardSevletContainer(ServletContext context) {
		super(context);
	}

	@Override
	public String getEnvironmentName() {
		return "Standard Servlet Container";
	}


	@Override
	public void init(WiringService wiringService) {
		// no-op
	}


}
