/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;


public interface Configurator<T extends Configuration> {


    public T getConfiguration(ConfigurationContext context);
}
