package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class UnexistantManagedFile extends AbstractContentManagedFile{

	
	private ManagedFile parent;
	private String name;
	
	public UnexistantManagedFile(ManagedFile parent, String name){
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		// no-op
	}

	@Override
	public ManagedFile doCreateFile() {
		return parent.createFile();
	}

	@Override
	public ManagedFile doCreateFolder() {
		return parent.createFolder();
	}

	@Override
	public boolean delete() {
		return true;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ManagedFileContent getContent() {
		return EmptyFileContent.getInstance();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManagedFile getParent() {
		return this.parent;
	}

	@Override
	public long getSize() throws ManagedIOException {
		return 0;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return new UnexistantManagedFile(this.parent,filepath);
	}

}
