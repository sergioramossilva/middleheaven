package org.middleheaven.namedirectory.jndi;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;

/**
 * Activates a JNDI based {@link NamingDirectoryException} service.
 */
public class JNDINamingDirectoryActivator extends Activator {

	private JNDINameDirectoryService service;
	
	/**
	 * 
	 * Constructor.
	 */
	public JNDINamingDirectoryActivator (){}
	
	@Publish
	public NameDirectoryService getNameDirectoryService(){
		return service;
	}
	
	@Override
	public void activate() {
		service =  new JNDINameDirectoryService();
	}

	@Override
	public void inactivate() {
		service = null;
	}

}
