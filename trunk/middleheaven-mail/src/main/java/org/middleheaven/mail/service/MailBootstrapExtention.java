package org.middleheaven.mail.service;

import org.middleheaven.core.bootstrap.BootstrapChain;
import org.middleheaven.core.bootstrap.BootstrapContainerExtention;
import org.middleheaven.core.bootstrap.ExecutionContext;

public class MailBootstrapExtention implements BootstrapContainerExtention {

	@Override
	public void extend(ExecutionContext context, BootstrapChain chain) {
		
		chain.doChain(context);
		
		if(!context.contains(JavaMailActivator.class)){
			context.addActivator(JavaMailActivator.class);
		}
	}

}
