package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public abstract class AbstractManagedRepository implements ManagedFileRepository {

	@Override
	public final boolean exists(String filename) throws ManagedIOException {
		return retrive(filename).exists();
	}
	
	@Override
	public final boolean delete(String filename) throws ManagedIOException {
		return delete(retrive(filename));
	}

	@Override
	public final boolean delete(ManagedFile file) throws ManagedIOException {
		if (!file.getParent().equals(this)){
			return false;
		}
		return file.delete();
	}

	@Override
	public final boolean isQueriable() {
		return QueryableRepository.class.isAssignableFrom(this.getClass());
	}
	
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		if (file.getParent().equals(this)){
			// already in store
			return;
		}
		
		if (!this.isWriteable()){
			throw new RepositoryNotWritableException(this.getClass().getName());
		}
		
		ManagedFile myFile = this.retrive(file.getName());
		if (!myFile.exists()){
			myFile = myFile.createFile();
		}
		
		if (file instanceof MediaManagedFile && myFile instanceof MediaManagedFile){
			((MediaManagedFile)myFile).getContent().setContentType(((MediaManagedFile)file).getContent().getContentType());
			((MediaManagedFile)myFile).getContent().setSize(((MediaManagedFile)file).getContent().getSize());
		}
		
		file.copyTo(myFile);
		
		
	}

	

}
