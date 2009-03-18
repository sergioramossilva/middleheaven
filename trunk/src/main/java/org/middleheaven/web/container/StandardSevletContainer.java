package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.WebContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;

public class StandardSevletContainer extends WebContainer{

	public StandardSevletContainer(ServletContext context) {
		super(context);
	}

	@Override
	public String getEnvironmentName() {
		return "Standard Servlet Container";
	}

	@Override
	public void init(ExecutionEnvironmentBootstrap bootstrap) {
		// no-op
	}

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		 this.context = null; 
	}

}
