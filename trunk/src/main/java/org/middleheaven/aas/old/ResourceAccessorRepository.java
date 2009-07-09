package org.middleheaven.aas.old;

import org.middleheaven.aas.Subject;

/**
 * Retreives the ResourceAcessor by a name
 */
public interface ResourceAccessorRepository {

	Subject findByName(String name);

}
