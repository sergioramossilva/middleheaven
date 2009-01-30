package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class EmptyFileRepository implements ManagedFileRepository {

	private static final EmptyFileRepository me = new EmptyFileRepository();
	
	public static EmptyFileRepository getRepository(){
		return me;
	}

	private EmptyFileRepository(){}
	

	@Override
	public boolean delete(String filename) throws ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}

	@Override
	public boolean delete(ManagedFile file) throws ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}

	@Override
	public boolean exists(String filename) throws ManagedIOException {
		return false;
	}

	@Override
	public boolean isQueriable() {
		return false;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		return new VoidManagedFile(null,filename);
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException,
			ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}

}
