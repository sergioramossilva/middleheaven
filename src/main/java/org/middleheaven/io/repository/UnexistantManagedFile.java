package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;

public class UnexistantManagedFile implements ManagedFile {

	
	private ManagedFile parent;
	private String name;
	
	protected UnexistantManagedFile(ManagedFile parent, String name){
		this.parent = parent;
		this.name = name;
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
	public ManagedFile createFile() {
		return parent.createFile();
	}

	@Override
	public ManagedFile createFolder() {
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
	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException {
		return Collections.emptySet();
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
