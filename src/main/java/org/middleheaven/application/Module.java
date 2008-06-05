/*
 * Created on 2006/10/28
 *
 */
package org.middleheaven.application;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;


import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallContext;
import org.middleheaven.util.Version;

public abstract class Module implements Serializable{

    private String ID;
    private String name;
    private String description;
    private String vendor;
    private Version version;
    private boolean isDefault;

    
    public String getParameter(String key){
        return null;
    }
    
    /**
     * Installs this module
     *
     */
    public abstract void install(InstallContext context);
    
    
    public Collection<Dependency> getDependencies(){
        return Collections.emptySet();
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the iD
     */
    public String getID() {
        return ID;
    }
    /**
     * @param id the iD to set
     */
    public void setID(String id) {
        ID = id;
    }
    /**
     * @return the isDefault
     */
    public boolean isDefault() {
        return isDefault;
    }
    /**
     * @param isDefault the isDefault to set
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }
    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    /**
     * @return the version
     */
    public Version getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(Version version) {
        this.version = version;
    }
    

}
