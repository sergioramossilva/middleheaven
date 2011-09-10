package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class UnexistantManagedFile extends AbstractContentManagedFile{

	
	private ManagedFile parent;

	public UnexistantManagedFile(ManagedFile parent, String name){
		super(new SimpleManagedFilePath(parent.getPath() , name));
		this.parent = parent;
	}
	
	@Override
	public void renameTo(String name) {
		this.setPath(new SimpleManagedFilePath(parent.getPath() , name));
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
	public ManagedFile retrive(String filename) throws ManagedIOException {
		return new UnexistantManagedFile(this,filename);
	}



}
