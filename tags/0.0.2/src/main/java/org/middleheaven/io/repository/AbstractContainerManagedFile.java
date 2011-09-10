package org.middleheaven.io.repository;


public abstract class AbstractContainerManagedFile extends AbstractManagedFile {

	protected AbstractContainerManagedFile(ManagedFilePath path) {
		super(path);
	}

	public ManagedFileContent getContent() {
		return EmptyFileContent.getInstance(); 
	}
}
