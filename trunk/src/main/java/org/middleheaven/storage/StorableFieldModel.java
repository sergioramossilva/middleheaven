package org.middleheaven.storage;

import org.middleheaven.domain.EntityFieldModel;

public interface StorableFieldModel extends EntityFieldModel{


	public QualifiedName getHardName();
	public String getParam(String string);

	StorableEntityModel getEntityModel();

}
