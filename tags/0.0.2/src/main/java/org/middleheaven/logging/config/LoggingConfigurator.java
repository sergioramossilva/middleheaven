/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.logging.config;

import org.middleheaven.logging.LoggingService;

public interface LoggingConfigurator {

    public void config(LoggingService configurator, LoggingConfiguration configuration);

}
