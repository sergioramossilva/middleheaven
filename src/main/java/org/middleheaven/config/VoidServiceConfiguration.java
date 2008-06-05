/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.middleheaven.dependency.Dependency;

public class VoidServiceConfiguration implements Configuration{

    public Collection<Dependency> getDependencies() {
        return Collections.emptySet();
    }

    public Number getNumberParameter(String name) {
        return null;
    }

    public Object getParameter(String name) {
        return null;
    }

    public String getStringParameter(String name) {
        return null;
    }

    public Date getTimeStampParameter(String name) {
        return null;
    }

}
