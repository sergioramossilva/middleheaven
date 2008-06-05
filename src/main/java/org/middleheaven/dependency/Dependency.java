/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;


public interface Dependency {

    /**
     * Informs if this dependency is necessary or optional.
     * @return true if this dependency is required.
     */
    public boolean isRequired();

    /**
     * 
     * @return the dependency class
     */
    public Class<?> getDependencyClass ();
}
