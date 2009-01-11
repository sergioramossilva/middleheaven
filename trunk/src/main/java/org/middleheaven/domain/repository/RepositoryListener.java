package org.middleheaven.domain.repository;

public interface RepositoryListener {

	public void onRepositoryChanged(RepositoryChangeEvent event);
}
