package org.middleheaven.domain.model;

import org.middleheaven.domain.model.EntityModelBuilder;
import org.middleheaven.domain.model.FieldModelBuilder;
import org.middleheaven.model.domain.repository.Repository;
import org.middleheaven.util.collections.EnhancedCollection;

public interface EntityModelBuilder<E> {

	
	public EnhancedCollection<FieldModelBuilder> fields();
	public FieldModelBuilder getField(String name);
	public EntityModelBuilder<E> setRepository(Repository<? extends E> repo);
	public EntityModelBuilder<E> setIdentityType(Class<?> type);
	public FieldModelBuilder getIdentityField();
	public Class<?> getIdentityType();

}
