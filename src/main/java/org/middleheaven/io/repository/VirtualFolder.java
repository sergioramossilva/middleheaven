package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class VirtualFolder extends AbstractContainerManagedFile  {

	private final Map<String,ManagedFile> files = new TreeMap<String,ManagedFile>();
	private final ManagedFile parent;
	private String name;
	
	public VirtualFolder(String name, ManagedFile parent){
		super(new SimpleManagedFilePath(parent.getPath() , name));
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public boolean contains(ManagedFile other) {
		return files.containsValue(other);
	}

	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		// TODO implement
		throw new UnsupportedOperationException();
	}

	@Override
	public ManagedFile doCreateFile() {
		// no-op
		return this;
	}

	@Override
	public ManagedFile doCreateFolder() {
		// no-op
		return this;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ManagedFileContent getContent() {
		return null;
	}

	@Override
	public ManagedFile getParent() {
		return parent;
	}

	@Override
	public long getSize() throws ManagedIOException {
		long sum =0;
		for (ManagedFile file : this.files.values()){
			sum+= file.getSize();
		}
		return sum;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
	}

	@Override
	public URL getURL() {
		// TODO implement ManagedFile.getURL
		return null;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public EnhancedCollection<ManagedFile> children() throws ManagedIOException {
		return CollectionUtils.enhance(files.values()).asUnmodifiable();
	}


	@Override
	public ManagedFile retrive(String filepath) {
		return this.files.get(filepath);
	}

	public void add(ManagedFile file) {
		this.files.put(file.getPath().getBaseName(),file);
		
	}

	public void remove(ManagedFile file) {
		this.files.remove(file.getPath().getBaseName());
	}

	public void clear() {
		for (ManagedFile file : files.values()){
			file.delete();
		}
		files.clear();
	}

	@Override
	public void renameTo(String name) {
		this.name = name;
	}




}
