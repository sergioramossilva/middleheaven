package org.middleheaven.model.domain.repository;

import org.middleheaven.domain.model.repository.RepositoryChangeEvent;

public interface RepositoryListener {

	public void onRepositoryChanged(RepositoryChangeEvent<?> event);
}
