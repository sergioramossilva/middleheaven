/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.logging.config;

import org.middleheaven.logging.ConfigurableLogListener;

public interface LoggingConfigurator {

    public void config(ConfigurableLogListener configurable, LoggingConfiguration configuration);

}
