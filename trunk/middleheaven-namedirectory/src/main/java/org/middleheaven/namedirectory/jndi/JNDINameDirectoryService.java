package org.middleheaven.namedirectory.jndi;

import java.lang.reflect.Modifier;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.middleheaven.core.reflection.SignatureProxy;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
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

			final ClassIntrospector<T> introspector = Introspector.of(type);
			
			if (introspector.isInstance(object)){
				return type.cast(object);
			} else if (object instanceof java.rmi.Remote){
				return type.cast(PortableRemoteObject.narrow(object, type));
			} else if (!Modifier.isFinal(type.getModifiers())){
				return introspector.newProxyInstance(new SignatureProxy(object));
			} else {
				throw new ClassCastException("Cannot cast " + object + " to " + type);
			}
		} catch (NamingException e) {
			throw new NamingDirectoryException(e);
		} catch (ClassCastException e) {
			throw new NamingDirectoryException(e);
		}


	}


}
