/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.core.reflection;

/**
 * @author  Sergio M. M. Taborda 
 */
public class InstantiationReflectionException extends ClassloadingException {

	private static final long serialVersionUID = -4863300763749876204L;
	
	protected final String className;
    public InstantiationReflectionException(String className,String message){
        super(message);
        this.className =  className;
    }

    public String getClassName() {
        return className;
    }


}
