package org.middleheaven.logging.writters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.util.StringUtils;

public class FileWriter extends StreamLogBookWriter {


    protected String filename;

    public FileWriter(){
        this.format = new HTMLFormat();
    }

    /**
     * Accepted Parameters
     * <ul>
     *  <li>file.name - log file name.</li>
     *  <li>file.append - logic value indicating if a new event must be aapended to the file, instead of overriding n existing file.</li>
     *  <li>file.path - path where to log the file. This pathe is relative to the path present in  LoggingConfiguration
     *  Writing permission is required for this path. </li>
     * </ul>
     */
    @Override 
    public void config(Map<String,String> params, LoggingConfiguration configuration) {
    	
    	
    	// determine if events must be appended to the file
    	 Boolean append = StringUtils.booleanValueOf(params.get("file.append"),true);

    	// determine file name   
        filename = params.get("file.name");
        if (filename==null || filename.trim().isEmpty()){
        	throw new IllegalArgumentException("file.name is required for FileWriter objetcs");
        }
        
        // determine file path, if any
        String filePath = params.get("file.path");
        if (filePath!=null && !filePath.trim().isEmpty()){
        	filename = filePath + "/" + filename;
        }
        
     
        ManagedFile rep = configuration.getBaseRepository();
        
        ManagedFile logFile = rep.retrive(filename);
        
        if (!logFile.exists()){
        	logFile.createFile();
        }

        File file = new File(logFile.getURL().toString());

        setFile(file,append.booleanValue());
    }


    protected void setFile(File file, boolean append){
        try {
            boolean exists = file.exists();
            this.out = new BufferedOutputStream(new FileOutputStream(file,append));
            if (!exists){
                this.format.writerHeader(out);
            }
        } catch (FileNotFoundException e) {
            // TODO
        }
    }

    public void finalized(){
        try {
            this.format.writerFooter(out);
            this.out.close();
        } catch (IOException e) {
            System.err.println("Problem finalizing " + this.getClass().getName() + "\n");
            e.printStackTrace(System.err);
        }
    }

}