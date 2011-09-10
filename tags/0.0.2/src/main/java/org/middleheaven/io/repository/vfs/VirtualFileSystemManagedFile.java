package org.middleheaven.io.repository.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileContent;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.WatchableContainer;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public final class VirtualFileSystemManagedFile extends AbstractManagedFile implements WatchableContainer {

	final FileObject file;
	final String finalPath;
	boolean isfilefolder;

	public VirtualFileSystemManagedFile(FileObject file){
		super(new VirtualFileManagedFilePath(file.getName()));
		this.file = file;
		try {	
			if (file.getType().hasContent()){
				final String ext = file.getName().getExtension();
				if (ext.equals("jar") || ext.equals("zip") || ext.equals("tar") || ext.equals("tgz")){
					finalPath = ext + ":" + file.getName().getURI().toString();
					isfilefolder = true;
				} else {
					isfilefolder = false;
					finalPath = file.getName().getURI().toString();
				}
			} else {
				isfilefolder = false;
				finalPath = file.getName().getURI().toString();
			}
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}

	public String toString(){
		return file.toString();
	}

	/**
	 *
	 * @return list of the ManagedFile existing in the repository 
	 */
	public EnhancedCollection<ManagedFile> children() throws ManagedIOException{

		if (this.getType() == ManagedFileType.FILE || !this.exists()){
			return CollectionUtils.emptyCollection();
		}

		try {
			FileObject[] files = getVirtualFolder().getChildren();
			Collection<ManagedFile> mfiles = new ArrayList<ManagedFile>();

			for (FileObject fo : files){
				mfiles.add(new VirtualFileSystemManagedFile(fo));
			}

			return CollectionUtils.enhance(mfiles);
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}



	public Collection<? extends  ManagedFile> listFiles(BooleanClassifier<ManagedFile> filter) throws ManagedIOException{
		if (this.getType() == ManagedFileType.FILE || !this.exists()){
			return Collections.emptySet();
		}

		try {

			FileObject[] files = getVirtualFolder().getChildren();
			Collection<ManagedFile> mfiles = new ArrayList<ManagedFile>();

			for (FileObject fo : files){
				ManagedFile mf = new VirtualFileSystemManagedFile(fo);
				if (filter.classify(mf)){
					mfiles.add(mf);
				}
			}

			return mfiles;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	private FileObject getVirtualFolder() throws FileSystemException{
		if (this.getType() == ManagedFileType.FOLDER){
			return this.file;
		} else {
			return VFS.getManager().resolveFile(this.finalPath);
		}
	}

	@Override
	public ManagedFileType getType() {
		try {



			if (file.getType().hasContent()){
				if (this.isfilefolder || file.getType().hasChildren()){
					return ManagedFileType.FILEFOLDER;
				} else {
					return ManagedFileType.FILE;
				}
			} else {
				if(file.exists()){
					return ManagedFileType.FOLDER;
				} else {
					return ManagedFileType.VIRTUAL;
				}
			}

		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public ManagedFile retrive(String filepath) {
		try{
			return new VirtualFileSystemManagedFile(getVirtualFolder().resolveFile(filepath));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isReadable() {
		try {
			return file.isReadable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isWriteable() {
		try {
			return file.isWriteable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}


	@Override
	public URL getURL() {
		try {
			return file.getURL();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}




	@Override
	public boolean exists() {
		try {
			return this.file.exists();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public ManagedFileContent getContent() {
		return new VirtualManagedFileContent();
	}

	@Override
	public ManagedFile getParent() {
		try{
			return new VirtualFileSystemManagedFile(file.getParent());
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}


	@Override
	public boolean contains(ManagedFile other) {
		return other.getParent().equals(this);
	}

	@Override
	public boolean equals(Object other){
		return getClass().isInstance(other) && ((VirtualFileSystemManagedFile)other).file.equals(this.file);
	}

	@Override
	public int hashCode(){
		return this.file.hashCode();
	}

	@Override
	public boolean delete() {
		try{
			return this.file.delete();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}



	private class VirtualManagedFileContent implements  ManagedFileContent{

		@Override
		public InputStream getInputStream() throws ManagedIOException{
			try {
				return file.getContent().getInputStream();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
		}

		@Override
		public OutputStream getOutputStream() throws ManagedIOException{
			try {
				return file.getContent().getOutputStream();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
		}

		@Override
		public long getSize() throws ManagedIOException {
			return VirtualFileSystemManagedFile.this.getSize();
		}

		@Override
		public boolean setSize(long size) throws ManagedIOException {
			return false;
		}

	}

	@Override
	public void addFileChangelistener(FileChangeListener listener,ManagedFile file) {
		try {
			this.file.getFileSystem().addListener( this.file.resolveFile(file.getPath().getBaseName()),new FileListenerAdapter(listener));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public void removeFileChangelistener(FileChangeListener listener,ManagedFile file) {
		try {
			this.file.getFileSystem().removeListener( this.file.resolveFile(file.getPath().getBaseName()),new FileListenerAdapter(listener));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isWatchable() {
		return this.getType().isFolder();
	}

	@Override
	public long getSize() throws ManagedIOException {
		try {
			return file.getContent().getSize();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public void renameTo(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ManagedFile doCreateFile() {
		try{
			this.file.createFile();
			return this;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}

	@Override
	protected ManagedFile doCreateFolder() {
		try{
			this.file.createFolder();
			return this;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}


}
