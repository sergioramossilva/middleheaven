package org.middleheaven.io.repository.vfs;

import org.apache.commons.vfs.FileName;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;

public class VirtualFileManagedFilePath implements ManagedFilePath {

	private FileName name;
	private ManagedFileRepository repository;
	
	public VirtualFileManagedFilePath(ManagedFileRepository repository, FileName name) {
		this.name = name;
		this.repository = repository;
	}

	@Override
	public String getBaseName() {
		return name.getBaseName();
	}

	@Override
	public int getNameCount() {
		return name.getDepth();
	}

	@Override
	public String getExtension() {
		return name.getExtension();
	}

	@Override
	public ManagedFilePath getParent() {
		return new VirtualFileManagedFilePath(repository, this.name.getParent());
	}

	@Override
	public String getPath() {
		return name.getPath();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return name.getBaseName() + "." + name.getExtension();
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof VirtualFileManagedFilePath) && equals( (VirtualFileManagedFilePath) other); 
	}
	
	public boolean equals(VirtualFileManagedFilePath other){
		return name.equals(other.name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository getManagedFileRepository() {
		return repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(int index) {
		// TODO
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return getName(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getRoot() {
		// TODO find root
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAbsolute() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolveSibling(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolve(ManagedFilePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath relativize(ManagedFilePath path) {
		// TODO Auto-generated method stub
		return null;
	}

}
