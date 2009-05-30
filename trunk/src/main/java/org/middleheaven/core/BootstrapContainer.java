/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core;


import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
public interface  BootstrapContainer  {


    public String getEnvironmentName();
    
    public void init(WiringService wiringService);
    public void start();
    public void stop();
    
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
     * TODO make into a repository
     * @return ManagedFile representing a folder where to read application classapth
     */
    public ManagedFile getAppClasspathRepository();


    
  
}
