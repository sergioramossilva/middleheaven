
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
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.SimpleManagedFilePath;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class DiskManagedFile  extends AbstractManagedFile implements QueryableRepository{

	private File file;
	private ManagedFile parent;

	public DiskManagedFile(ManagedFile parent, File file) {
		super(new SimpleManagedFilePath(readPath(parent), ensureNotNull(file).getName()));
		this.file = file;
		this.parent = parent;
	}
	
	private static ManagedFilePath readPath(ManagedFile parent){
		if (parent == null){
			return null;
		}
		return parent.getPath();
	}
	
	private static File ensureNotNull(File file){
		if(file == null){
			throw new IllegalArgumentException("File is null");
		}
		return file;
	}

	public boolean equals(Object other){
		return other instanceof DiskManagedFile && ((DiskManagedFile)other).file.equals(this.file);
	}
	
	public int hashCode (){
		return file.hashCode();
	}
	
	public String toString(){
		return file.toString();
	}
	
	@Override
	public boolean contains(ManagedFile other) {
		return this.resolveFile(other.getPath().getBaseName()).exists();
	}

	@Override
	public boolean delete() {
		return file.delete();
	}

	@Override
	public boolean exists() {
		return file.exists();
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
					return new FileInputStream(file);
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public OutputStream getOutputStream() throws ManagedIOException {
				try {
					return new FileOutputStream(file);
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
		if (!this.file.exists()){
			return 0;
		}
		
		if (this.file.getParent()==null ){
			// drive
			return this.file.getTotalSpace() - this.file.getFreeSpace();  
		} else if(this.file.isDirectory()) {
			// folder
			return  getDirSize(file);
		} 
		// file
		return this.file.length();
		
		
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
	public ManagedFile getParent() {
		if (parent !=null && file.getParentFile() !=null){
			return new DiskManagedFile(null,file.getParentFile());
		}
		return parent;
	}


	@Override
	public ManagedFileType getType() {
		return this.file.exists() ? ( this.file.isFile() ? ManagedFileType.FILE : ManagedFileType.FOLDER) : ManagedFileType.VIRTUAL;
	}

	@Override
	public URL getURL() {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw ManagedIOException.manage(e);
		}
	}

	@Override
	public boolean isReadable() {
		return file.canRead();
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return file.canWrite();
	}

	@Override
	public EnhancedCollection<ManagedFile> children() throws ManagedIOException {
		File[] children = file.listFiles();

		if (children == null){ // not a folder
			return CollectionUtils.emptyCollection();
		}
		
		Collection<ManagedFile> all = new ArrayList<ManagedFile>(children.length);
		for (File f : children){
			all.add(new DiskManagedFile(DiskManagedFile.this,f));	
		}
		return CollectionUtils.enhance(all);
	}


	@Override
	public void renameTo(String name) {
		file.renameTo(new File(file.getParent(), name));
	}

	private ManagedFile resolveFile(String filepath) {
		return new DiskManagedFile(this,new File(this.file , filepath).getAbsoluteFile());
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
		return this.retrive(file.getPath().getBaseName()).delete();
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
		
		ManagedFile myFile = this.resolveFile(file.getPath().getBaseName());
		if (!myFile.exists()){
			myFile = myFile.createFile();
		}
		file.copyTo(myFile);
	}

	@Override
	protected ManagedFile doCreateFile() {
		try {
			file.createNewFile();
			return new DiskManagedFile(this.parent,file);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}

	}

	@Override
	protected ManagedFile doCreateFolder() {

		file.mkdirs();
		return new DiskManagedFile(this.parent,file);

	}

}
