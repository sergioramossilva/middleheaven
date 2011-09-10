package org.middleheaven.core.bootstrap;

import java.util.List;

import org.middleheaven.logging.Log;

public class BootstrapChain {

	List<BootstrapContainerExtention> extentions;
	private int current=0;
	private BootstrapContainer container;
	
	public BootstrapChain(List<BootstrapContainerExtention> extentions, BootstrapContainer container){
		this.extentions  = extentions;
		this.container = container;
	}
	
	
	public void doChain(BootstrapContext context) {
		if (current<extentions.size()){
			current++;

			final BootstrapContainerExtention item = extentions.get(current-1);
			try {
				// invoque 
				
				item.extend(context, this);
				
			} catch (Exception e){
				Log.onBookFor(this.getClass()).error(e,"Exception found handling {0}", item.getClass());
			} catch (Error e){
				Log.onBookFor(this.getClass()).fatal(e,"Exception found handling interceptor {0}" , item.getClass());

			}
			
		} else {
			container.configurate(context);
		}
	}

}
