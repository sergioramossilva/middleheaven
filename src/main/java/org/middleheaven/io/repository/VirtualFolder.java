package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;

public class VirtualFolder extends AbstractContainerManagedFile  {

	private final Map<String,ManagedFile> files = new TreeMap<String,ManagedFile>();
	private final ManagedFile parent;
	private String name;
	
	public VirtualFolder(String name, ManagedFile parent){
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
	public String getName() {
		return name;
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
	public EnhancedCollection<ManagedFile> listFiles() throws ManagedIOException {
		return Collections.unmodifiableCollection(files.values());
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(BooleanClassifier<ManagedFile> filter) throws ManagedIOException {
		return Collections.unmodifiableCollection(files.values());
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return this.files.get(filepath);
	}

	public void add(ManagedFile file) {
		this.files.put(file.getName(),file);
		
	}

	public void remove(ManagedFile file) {
		this.files.remove(file.getName());
	}

	public void clear() {
		for (ManagedFile file : files.values()){
			file.delete();
		}
		files.clear();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}




}
