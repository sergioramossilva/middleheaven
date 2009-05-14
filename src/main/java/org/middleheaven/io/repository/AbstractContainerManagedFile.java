package org.middleheaven.io.repository;


public abstract class AbstractContainerManagedFile extends AbstractManagedFile {

	public ManagedFileContent getContent() {
		return EmptyFileContent.getInstance(); 
	}
}
