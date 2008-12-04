package org.middleheaven.aas;

import java.util.HashMap;
import java.util.Iterator;

public class PermissionSet implements Permission {

    private HashMap<String,ResourcePermission> permissions;
    
    public PermissionSet(){
    	permissions = new HashMap<String,ResourcePermission>(); 
    }

    public void add(ResourcePermission p){
        permissions.put(p.resourceName,p);
    }

    public int size(){
        return permissions.size();
    }

    public boolean equals(Object other){
        return other instanceof PermissionSet && ((PermissionSet)other).permissions.equals(this.permissions);
    }

    public int hashCode(){
        return permissions.hashCode();
    }

    public boolean implies(Permission threshold) {
        if (threshold.isLenient()){
            return true;
        } else if (threshold.isStrict()){
            return false;
        } else if (threshold instanceof PermissionSet){
            // verifica que todos os recursos no threshold estão inclusos nos
            // recursos deste

            for (Iterator<ResourcePermission> it = ((PermissionSet)threshold).iterator();it.hasNext();){
                ResourcePermission t = (ResourcePermission) it.next();
                ResourcePermission r = (ResourcePermission)this.permissions.get(t.resourceName);
                if (r==null && t.permissionLevel!=PermissionLevel.NONE){ // faltou um recuro e é requerido
                    return false;
                }
                if (!PermissionLevel.levelIncludes(r.permissionLevel , t.permissionLevel)){
                    return false;
                }
            }
            return true;
        } else {
            if (((ResourcePermission)threshold).permissionLevel == PermissionLevel.NONE){
                return true; // se não é requerido nenhum nivel, aceita
            }
            
            for (ResourcePermission r : permissions.values()){
                if (r.implies(threshold)){
                       return true;
                }
            }
            return false;
        }
    }

    Iterator<ResourcePermission> iterator(){
       return permissions.values().iterator();
    }

    public boolean isLenient() {
        return false;
    }
    
    public String toString (){
        StringBuilder buffer = new StringBuilder('[');
        for (Permission p :  permissions.values()){
            buffer.append(p.toString());
            buffer.append(';');
        }
        return buffer.append(']').toString();

    }

    public boolean isStrict() {
        return false;
    }

}
