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
     * @return <code>true</code> se o <code>ResourceAcessor</code> não
     * necessita de nenhuma permissão especial para satisfazer esta permissão.
     * Ou seja, todos os  <code>ResourceAcessor</code> têm acesso ao recurso protegido
     * por esta permissão.
     */
    public boolean isLenient();
    
    /**
     * 
     * @return  <code>true</code> se nenhuma permissão do <code>ResourceAcessor</code> 
     *  satisfazer esta permissão independente de quais as permissões do <code>ResourceAcessor</code> 
     * Ou seja, nenhum <code>ResourceAcessor</code> têm acesso ao recurso protegido
     * por esta permissão.
     */
    public boolean isStrict();
    
}
