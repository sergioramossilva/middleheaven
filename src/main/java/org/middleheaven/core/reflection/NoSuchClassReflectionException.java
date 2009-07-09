/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.core.reflection;

/**
 * @author  Sergio M. M. Taborda 
 */
public class NoSuchClassReflectionException extends ClassloadingException {

    protected final String className;
    public NoSuchClassReflectionException(String className){
        super(className + " could not be found on the classpath");
        this.className =  className;
    }


    public String getClassName() {
        return className;
    }


}
