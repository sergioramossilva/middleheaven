package org.middleheaven.domain.model;

import org.middleheaven.domain.model.ModelBuilder;

public interface DomainModelReader {

	
	public void read(Class<?> type, ModelBuilder builder);
}
