package org.middleheaven.core.bootstrap;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;

public class JWSBootstrap extends ExecutionEnvironmentBootstrap{

	public static void main(String[] args){
		JWSBootstrap bootstrap = new JWSBootstrap();
		bootstrap.start();
	}
	
	@Override
	public Container getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContextIdentifier getContextIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

}
