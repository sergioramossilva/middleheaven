package org.middleheaven.io.repository;

import java.net.URL;

import org.middleheaven.io.ManagedIOException;

public class ClassPathRepository implements ManagedFileRepository{



	/**
	 * @param type the base classpath class
	 * @return a ClassPathRepository relative to the package of the class passed as argument.
	 * 
	 */
	public static ClassPathRepository repositoryFor(Class<?> type){
		return new ClassPathRepository(type);
	}

	/**
	 * 
	 * @return a ClassPathRepository relative to the the package of the calling class.
	 */
	public static ClassPathRepository repositoryFor() {
		Exception e = new Exception();
		StackTraceElement elm = e.getStackTrace()[1];

		try {
			return new ClassPathRepository(Class.forName(elm.getClassName()));
		} catch (ClassNotFoundException e1) {
			throw new ManagedIOException(e);
		}
	}

	Class<?> type;

	private ClassPathRepository(Class<?> type){
		this.type = type;
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}

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
		return retrive(filename).exists();
	}

	@Override
	public boolean isQueriable() {
		return false;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		URL url =  type.getResource(filename);
		if ( url ==null){
			return new UnexistantManagedFile(null, filename);
		}

		return ManagedFileRepositories.resolveFile(url);

	}



}
