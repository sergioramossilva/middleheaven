package org.middleheaven.aas;

import java.util.Iterator;

/**
 * Condition with with the ResourceAccessor must comply 
 * in order to access a specific resource.
 */
public class ResourcePermission implements Permission {

	PermissionLevel permissionLevel;
    String resourceName;

    public static Permission getInstance(String resourceName, PermissionLevel permissionLevel){
        return new ResourcePermission( resourceName, permissionLevel );
    }

    public static Permission getInstance(String resourceName){
        return new ResourcePermission( resourceName, PermissionLevel.READ );
    }

    private ResourcePermission(String resourceName, PermissionLevel permissionLevel){
    	this.resourceName = resourceName;
    	this.permissionLevel = permissionLevel;
    }
    
    /**
     *
     * @return o nome do recurso ao qual esta permissão diz respeito
     */
	public String getResourceName() {
		return resourceName;
	}


    public boolean equals(Object other){
        return other instanceof ResourcePermission &&
            ((ResourcePermission)other).permissionLevel == this.permissionLevel &&(
                    this.resourceName == null && ((ResourcePermission)other).resourceName==null ||
                    ((ResourcePermission)other).resourceName.equals(this.resourceName)
            );
    }

    public int hashCode(){
        return permissionLevel.hashCode() ^ resourceName.hashCode();
    }

    public PermissionLevel getLevel(){
        return this.permissionLevel;
    }

    public boolean implies(Permission threshold) {
        if (threshold.isLenient()){
            return true;
        } else if (threshold.isStrict()){
            return false;
        } else if (threshold instanceof PermissionSet){
            PermissionSet set = (PermissionSet) threshold;
            if (set.size()>1){
                return false; // a unique permission cannot contain more than one
            } else {
                for (Iterator<ResourcePermission> it = set.iterator();it.hasNext();){
                    ResourcePermission r = it.next();
                    if (r.resourceName.equals(this.resourceName)&& PermissionLevel.levelIncludes(this.permissionLevel, r.permissionLevel)){
                           return true;
                    }
                }
                return false;
            }
        } else {

           ResourcePermission other = (ResourcePermission)threshold;
           if (other.permissionLevel == PermissionLevel.NONE){
               return true; // se não é requerido nenhum nivel, aceita
           }
           return other.resourceName.equals(this.resourceName) && PermissionLevel.levelIncludes(this.permissionLevel, other.permissionLevel);
        }
    }

    public String toString(){
        return this.resourceName + "=>" + this.permissionLevel;
    }

    public boolean isLenient() {
        return this.permissionLevel == PermissionLevel.NONE;
    }

    public boolean isStrict() {
        return "resource.null".equals(resourceName);
    }
}

