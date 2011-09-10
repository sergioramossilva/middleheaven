package org.middleheaven.model;

import org.middleheaven.domain.model.ModelSetItemBuilder;

/**
 * Reads the class model.
 */
public interface ModelReader< B extends ModelSetItemBuilder> {

	public void read(Class<? extends Object> type, B  builder);
}
