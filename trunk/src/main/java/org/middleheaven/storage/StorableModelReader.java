package org.middleheaven.storage;

import org.middleheaven.domain.EntityModel;

public interface StorableModelReader {

	StorableEntityModel read(EntityModel model);

}
