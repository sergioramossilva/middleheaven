package org.middleheaven.aas;


public interface ResourceAcessor {

    public String getName();
    public boolean hasPermission(Permission permit);

}
