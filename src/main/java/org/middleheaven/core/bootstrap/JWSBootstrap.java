package org.middleheaven.core.bootstrap;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class JWSBootstrap extends ExecutionEnvironmentBootstrap{

	public static void main(String[] args){
		JWSBootstrap bootstrap = new JWSBootstrap();
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
	}
	
	@Override
	public Container getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContextIdentifier getContextIdentifier() {
		return ContextIdentifier.getInstance(System.getProperty("middleheaven.application.id"));
	}

}
