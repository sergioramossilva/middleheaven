
package org.middleheaven.io.repository.container;

/**
 * 
 * This class implements <code>CharSequence</code> so it can 
 * be used in FileReposity Service to retrieve repositories.
 * 
 *
 */
public enum CommonRepositories implements CharSequence{

    APP_IMAGE, // Image repository
    APP_ROOT, // Root repository
    APP_DATA, // Data repository. Generic repository
    APP_CONFIG, // Application Configuration
    APP_TEMP_DATA, // Temporary Data Repository
    APP_CONFIGURATION, // Application Configuration Repository
    ENV_CONFIGURATION, // Execution Environment Configuration Repository
    ENV_DATA,
    APP_LOG, // Log Repository
    APP_CLASSPATH; // Class repository
    
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
