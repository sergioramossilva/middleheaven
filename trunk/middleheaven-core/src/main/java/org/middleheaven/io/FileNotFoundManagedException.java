/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.File;



public class FileNotFoundManagedException extends ManagedIOException {

    
  
    protected String filename;
    public FileNotFoundManagedException(File file){
        this(file.getAbsolutePath());
    }
    
    public FileNotFoundManagedException(String filename){
        super(filename + "was not found.");
        this.filename = filename;
    }
    
    
    public String getFileName(){
        return filename;
    }
}