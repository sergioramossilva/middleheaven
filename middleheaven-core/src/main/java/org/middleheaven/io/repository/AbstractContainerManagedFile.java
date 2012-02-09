package org.middleheaven.io.repository;

import org.middleheaven.io.repository.empty.EmptyFileContent;


public abstract class AbstractContainerManagedFile extends AbstractManagedFile {

	protected AbstractContainerManagedFile(ManagedFileRepository repository) {
		super(repository);
	}

	public ManagedFileContent getContent() {
		return EmptyFileContent.getInstance(); 
	}
}
