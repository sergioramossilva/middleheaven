package org.middleheaven.core.bootstrap;

import java.util.List;

import org.middleheaven.logging.Logger;

public class BootstrapChain {

	List<BootstrapContainerExtention> extentions;
	private int current=0;
	private BootstrapContainer container;
	private Logger logger;
	
	public BootstrapChain(Logger logger, List<BootstrapContainerExtention> extentions, BootstrapContainer container){
		this.extentions  = extentions;
		this.container = container;
		this.logger = logger;
	}
	
	
	public void doChain(ExecutionContext context) {
		if (current<extentions.size()){
			current++;

			final BootstrapContainerExtention item = extentions.get(current-1);
			try {
				// invoque 
				
				item.extend(context, this);
				
			} catch (Exception e){
				logger.error(e,"Exception found handling {0}", item.getClass());
			} catch (Error e){
				logger.fatal(e,"Exception found handling interceptor {0}" , item.getClass());

			}
			
		} else {
			container.configurate(context);
		}
	}

}
