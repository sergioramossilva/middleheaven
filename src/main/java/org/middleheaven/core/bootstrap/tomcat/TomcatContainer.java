/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.core.bootstrap.tomcat;

import javax.servlet.ServletContext;

import org.middleheaven.core.WebContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;

public class TomcatContainer extends WebContainer  {


    public TomcatContainer(ServletContext context){
        super(context);
    }
    
    @Override
    public String getEnvironmentName() {
        // TODO pegar detalhes, versão OS ,etc..
        return "Tomcat";
    }

    @Override
    public void init(ExecutionEnvironmentBootstrap bootstrap) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(ExecutionEnvironmentBootstrap bootstrap) {
        this.context = null; 
    }






}
