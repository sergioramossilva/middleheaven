/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;





/**
 * The real container to complete the bootstrap cycle.
 */
public interface  BootstrapContainer  {


    public String getContainerName();
    
    public ContainerFileSystem getFileSystem();
    
    public void configurate(BootstrapContext context);
    
    public void start();
    public void stop();
    

}
