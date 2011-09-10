package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.Version;

public class StandardSevletContainer extends WebContainer{

	public StandardSevletContainer(ServletContext context, ManagedFile root) {
		super(context, root);
	}

	@Override
	public String getContainerName() {
		return  "Standard Servlet Container";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		return new WebContainerInfo(CommonHttpServerContainers.UNKNOW, Version.unknown());
	}






}
