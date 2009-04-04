package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;

public class VoidManagedFile implements ManagedFile {

	private String name;
	private ManagedFile parent;
	
	public VoidManagedFile(ManagedFile parent, String name){
		this.name = name;
		this.parent = parent;
	}
	
	@Override
	public boolean contains(ManagedFile other) {
		return false;
	}

	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		// no-op
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
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManagedFile getParent() {
		return parent;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
	}

	@Override
	public URL getURL() {
		return null;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public Collection<? extends ManagedFile> listFiles()
			throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter)
			throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return new VoidManagedFile(this,filepath);
	}

	@Override
	public long getSize() throws ManagedIOException {
		return 0;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ManagedFile createFile() throws UnsupportedOperationException {
		return this;
	}

	@Override
	public ManagedFile createFolder() throws UnsupportedOperationException {
		return this;
	}

}
