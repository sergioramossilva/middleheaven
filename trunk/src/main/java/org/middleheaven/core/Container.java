/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core;


import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;

/**
 * @author  Sergio M.M. Taborda
 */
public interface  Container  {


    public String getEnvironmentName();
    
    public void init(ExecutionEnvironmentBootstrap bootstrap);
    public void start(ExecutionEnvironmentBootstrap bootstrap);
    public void stop(ExecutionEnvironmentBootstrap bootstrap);
    
    /**
     * 
     * @return ManagedFile representing a folder where to read environment configuration classapth
     */
    public ManagedFile getEnvironmentConfigRepository();
    
    
    /**
     * 
     * @return ManagedFile representing a folder where to read application configuration
     */
    public ManagedFile getAppConfigRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application data
     */
    public ManagedFile getAppDataRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application log
     */
    public ManagedFile getAppLogRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read application classapth
     */
    public ManagedFile getAppClasspathRepository();
    
  
}
