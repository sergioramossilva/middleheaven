package org.middleheaven.domain;

import org.middleheaven.domain.repository.Repository;
import org.middleheaven.util.identity.Identity;

public interface EntityModelBuilder<E> {

	public FieldModelBuilder getField(String name);
	public EntityModelBuilder<E> setRepository(Repository<? extends E> repo);
	public EntityModelBuilder<E> setIdentityType(Class<? extends Identity> type);

}
