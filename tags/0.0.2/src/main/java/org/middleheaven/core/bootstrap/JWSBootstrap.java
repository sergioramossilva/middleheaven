package org.middleheaven.core.bootstrap;

import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class JWSBootstrap extends ExecutionEnvironmentBootstrap{

	public static void main(String[] args){
		JWSBootstrap bootstrap = new JWSBootstrap();
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
	}
	
	@Override
	public BootstrapContainer getContainer() {
		// TODO filesystme must be acessible from JWS
		return new DesktopUIContainer(null);
	}


}
