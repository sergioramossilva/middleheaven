package org.middleheaven.aas;


public interface ResourceAcessor {

    public Object getID();
    public boolean hasPermission(Permission permit);

}
