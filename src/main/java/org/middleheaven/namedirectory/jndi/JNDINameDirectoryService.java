package org.middleheaven.namedirectory.jndi;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;

@Service
public class JNDINameDirectoryService implements NameDirectoryService{

	private Properties properties;

	
	
	public JNDINameDirectoryService(){
		this.properties = System.getProperties();
	}
	
	public JNDINameDirectoryService(Properties properties){
		this.properties = properties;
	}

	public Properties getProperties(){
		return properties;
	}

	public InitialContext getContext() throws NamingException{
		return new InitialContext();
	}

	public <T> T lookup(String name, Class<T> type) throws NamingDirectoryException {

		Object object;
		try {
			object = getContext().lookup(name);
			if(object==null){
				return null;
			}

			if (type.isInstance(object)){
				return type.cast(object);
			} else {
				return type.cast(PortableRemoteObject.narrow(object, type));
			}
		} catch (NamingException e) {
			throw new NamingDirectoryException(e);
		}


	}


}
