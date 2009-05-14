package org.middleheaven.io.repository.simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.EmptyFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileContent;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class DiskManagedFile  extends AbstractManagedFile implements ManagedFileRepository , QueryableRepository{

	private File root;
	private ManagedFile parent;

	public DiskManagedFile(ManagedFile parent, File root) {
		this.root = root;
		this.parent = parent;
	}

	@Override
	public boolean contains(ManagedFile other) {
		return this.resolveFile(other.getName()).exists();
	}

	@Override
	public boolean delete() {
		return root.delete();
	}

	@Override
	public boolean exists() {
		return root.exists();
	}

	@Override
	public ManagedFileContent getContent() {
		if (this.getType().equals(ManagedFileType.FOLDER)){
			return EmptyFileContent.getInstance(); 
		}

		return new ManagedFileContent(){

			@Override
			public InputStream getInputStream() throws ManagedIOException {
				try {
					return new FileInputStream(root);
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public OutputStream getOutputStream() throws ManagedIOException {
				try {
					return new FileOutputStream(root);
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public long getSize() throws ManagedIOException {
				return DiskManagedFile.this.getSize();
			}

			@Override
			public boolean setSize(long size) throws ManagedIOException {
				// not supported. fail silently
				return false;
			}

		};
	}

	@Override
	public final long getSize() {
		if (!this.root.exists()){
			return 0;
		}
		
		if (this.root.getParent()==null ){
			// drive
			return this.root.getTotalSpace() - this.root.getFreeSpace();  
		} else if(this.root.isDirectory()) {
			// folder
			return  getDirSize(root);
		} 
		// file
		return this.root.length();
		
		
	}

	private long getDirSize(File dir) {
		long size = 0;

		if (dir.isFile()) {
			size = dir.length();
		} else {
			File[] subFiles = dir.listFiles();

			for (File file : subFiles) {
				size += this.getDirSize(file);
			}
		}
		return size;
	}

	@Override
	public String getName() {
		return root.getName();
	}

	@Override
	public ManagedFile getParent() {
		if (parent !=null && root.getParentFile() !=null){
			return new DiskManagedFile(null,root.getParentFile());
		}
		return parent;
	}


	@Override
	public ManagedFileType getType() {
		return this.root.isFile() ? ManagedFileType.FILE : ManagedFileType.FOLDER;
	}

	@Override
	public URL getURL() {
		try {
			return root.toURI().toURL();
		} catch (MalformedURLException e) {
			throw ManagedIOException.manage(e);
		}
	}

	@Override
	public boolean isReadable() {
		return root.canRead();
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return root.canWrite();
	}

	@Override
	public EnhancedCollection<ManagedFile> listFiles() throws ManagedIOException {
		File[] children = root.listFiles();

		Collection<ManagedFile> all = new ArrayList<ManagedFile>(children.length);
		for (File f : children){
			all.add(new DiskManagedFile(DiskManagedFile.this,f));	
		}
		return CollectionUtils.enhance(all);
	}


	@Override
	public void setName(String name) {
		root.renameTo(new File(root.getParent(), name));
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return new DiskManagedFile(this,new File(filepath));
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		return resolveFile(filename);
	}

	@Override
	public boolean delete(String filename) throws ManagedIOException {
		return resolveFile(filename).delete();
	}

	@Override
	public boolean delete(ManagedFile file) throws ManagedIOException {
		return this.retrive(file.getName()).delete();
	}

	@Override
	public boolean exists(String filename) throws ManagedIOException {
		return this.retrive(filename).exists();
	}

	@Override
	public boolean isQueriable() {
		return true;
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		if (file.getParent().equals(this)){
			// already in store
			return;
		}
		
		if (!this.isWriteable()){
			throw new RepositoryNotWritableException(this.getClass().getName());
		}
		
		ManagedFile myFile = this.resolveFile(file.getName());
		if (!myFile.exists()){
			myFile = myFile.createFile();
		}
		file.copyTo(myFile);
	}

	@Override
	protected ManagedFile doCreateFile() {
		try {
			root.createNewFile();
			return new DiskManagedFile(this.parent,root);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}

	}

	@Override
	protected ManagedFile doCreateFolder() {

		root.mkdirs();
		return new DiskManagedFile(this.parent,root);

	}

}
