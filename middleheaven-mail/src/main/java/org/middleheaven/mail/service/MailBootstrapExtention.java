package org.middleheaven.mail.service;

import org.middleheaven.core.bootstrap.BootstrapChain;
import org.middleheaven.core.bootstrap.BootstrapContainerExtention;
import org.middleheaven.core.bootstrap.BootstrapContext;

public class MailBootstrapExtention implements BootstrapContainerExtention {

	@Override
	public void extend(BootstrapContext context, BootstrapChain chain) {
		
		chain.doChain(context);
		
		if(!context.contains(JavaMailActivator.class)){
			context.addActivator(JavaMailActivator.class);
		}
	}

}
