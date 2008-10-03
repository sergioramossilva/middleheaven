package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class EmptyFileRepository implements ManagedFileRepository {

	
	public EmptyFileRepository(){}
	
	@Override
	public ManagedFile create(String filename) throws ManagedIOException {
		return null;
	}

	@Override
	public boolean delete(String filename) throws ManagedIOException {
		return true;
	}

	@Override
	public boolean delete(ManagedFile file) throws ManagedIOException {
		return true;
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
		return null;
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException,
			ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}

}
