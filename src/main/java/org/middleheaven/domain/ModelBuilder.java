package org.middleheaven.domain;

public interface ModelBuilder {

	public <E> EntityModelBuilder<E> getEntity(Class<E> type);

}
