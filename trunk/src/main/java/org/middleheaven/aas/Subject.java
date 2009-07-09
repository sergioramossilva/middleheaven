package org.middleheaven.aas;



public interface Subject {

    public String getName();
    public boolean hasPermission(Permission permit);

}
