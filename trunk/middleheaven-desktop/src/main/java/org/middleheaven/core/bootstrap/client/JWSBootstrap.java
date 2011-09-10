package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class JWSBootstrap extends ExecutionEnvironmentBootstrap{

	public static void main(String[] args){
		JWSBootstrap bootstrap = new JWSBootstrap();
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
	}
	
	@Override
	public BootstrapContainer resolveContainer(ManagedFile rooFolder) {
		return new DesktopUIContainer(rooFolder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile getEnvironmentRootFolder() {
		// TODO filesystme must be acessible from JWS
		return null;
	}



}
