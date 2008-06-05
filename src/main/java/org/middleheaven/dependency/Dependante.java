/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.dependency;

import java.util.Collection;

public interface Dependante {

    public Collection<Dependency> getDependencies();
    
}
