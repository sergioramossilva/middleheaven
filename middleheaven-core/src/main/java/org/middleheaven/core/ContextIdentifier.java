/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.core;

import java.io.Serializable;

public class ContextIdentifier implements Serializable {

    
    
    private final String id;
    protected ContextIdentifier(String ID){
        this.id = ID;
    }
    
    public static ContextIdentifier getInstance(CharSequence id) {
        return new ContextIdentifier(id.toString());
        
    }
    
    public String toString(){
        return id;
    }
    
    public boolean equals(Object other){
        return other instanceof ContextIdentifier && id !=null && id.equals(other.toString());
    }
}
