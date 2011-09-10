/*
 * Created on 16/08/2005
 */
package org.middleheaven.logging.writters;

import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;

/**
 *  @author Sergio M. M. Taborda
 */
public class ConsoleLogWriter extends StreamLogBookWriter {

    public ConsoleLogWriter(){
        this.out = System.out;
    }

    /**
     * @see br.com.gnk.fw.logging.LogWriter#config(java.util.Map, LoggingConfiguration)
     */
    public void config(Map params, LoggingConfiguration configuration) {}

}
