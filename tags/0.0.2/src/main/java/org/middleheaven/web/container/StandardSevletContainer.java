package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.ContainerFileSystem;

public class StandardSevletContainer extends WebContainer{

	public StandardSevletContainer(ServletContext context) {
		super(context);
	}

	@Override
	public String getContainerName() {
		return  "Standard Servlet Container";
	}




}
