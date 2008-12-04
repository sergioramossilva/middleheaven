package org.middleheaven.aas;

import java.io.Serializable;

/**
 * Condition with with the ResourceAccessor must comply 
 * in order to access a resource.
 *
 */
public interface Permission extends Serializable{

    
	/**
	 * 
	 * @param required the required permission
	 * @return <true> if this permission implies the access required by <code>required</code>
	 */
    public boolean implies(Permission required);
    
    /**
     * 
     * @return <code>true</code> if the <code>ResourceAcessor</code> does not 
     * need any special permission to satisfy this permission, i.e.
     * all <code>ResourceAcessor</code> have access to the resource protected by this permission.
     * 
     */
    public boolean isLenient();
    
    /**
     * 
     * @return  <code>true</code> if no <code>ResourceAcessor</code> permission
     * can satisfy this permissions independently of witch permissions the <code>ResourceAcessor</code> has,
     * i.e. no <code>ResourceAcessor</code> can have acess to the resouce protected by this permission.
     * 
     */
    public boolean isStrict();
    
}
