/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.io.repository.service;

/**
 * 
 * This class implements <code>CharSequence</code> so it can 
 * be used in FileReposity Service to retrieve repositories.
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public enum CommonRepositories implements CharSequence{

    IMAGE, // Image repository
    DATA, // Data repository. Generic repository
    TEMP_DATA, // Temporary Data Repository
    APP_CONFIGURATION, // Application Configuration Repository
    ENV_CONFIGURATION, // Execution Environment Configuration Repository
    LOG; // Log Repository

    
    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    public int length() {
        return this.toString().length();
    }

    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.toString().subSequence(beginIndex, endIndex);
    } 
    
    
   
    
}
