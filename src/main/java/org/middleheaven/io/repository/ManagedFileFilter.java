/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import org.middleheaven.classification.BooleanClassifier;

public interface ManagedFileFilter extends BooleanClassifier<ManagedFile>{

    
    public Boolean classify(ManagedFile file);
}
