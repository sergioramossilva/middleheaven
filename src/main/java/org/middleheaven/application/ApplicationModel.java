/*
 * Created on 2006/10/28
 *
 */
package org.middleheaven.application;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.util.Version;

public class ApplicationModel implements Serializable {

    private String ID;
    private String name;
    private String vendor;
    private String description;
    private Version version;
    
    private Set<Module> modules = new HashSet<Module>();

    
    public ApplicationModel(String ID){
        this.ID = ID;
    }
    
    
    void addModule(Module module){
        modules.add(module);
    }
    
    void removeModule(Module module){
        modules.add(module);
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
