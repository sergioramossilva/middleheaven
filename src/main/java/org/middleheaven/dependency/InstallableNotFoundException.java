/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;

public class InstallableNotFoundException extends RuntimeException {

    
    public InstallableNotFoundException(String installableID){
        super("Installable " + installableID + " was not found" );
    }
}
