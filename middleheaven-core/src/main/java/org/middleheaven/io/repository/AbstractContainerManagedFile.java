package org.middleheaven.io.repository;


public abstract class AbstractContainerManagedFile extends AbstractManagedFile {

	protected AbstractContainerManagedFile(ManagedFileRepository repository) {
		super(repository);
	}

	public ManagedFileContent getContent() {
		return EmptyFileContent.getInstance(); 
	}
}
