package org.middleheaven.io.repository.vfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileUtil;
import org.middleheaven.io.IOUtils;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.RepositoryNotQueriableException;
import org.middleheaven.io.repository.RepositoryNotRedableException;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.WatchableContainer;

public class VirtualFileSystemManagedRepository implements ManagedFileRepository,QueryableRepository,WatchableContainer{

	private FileObject root;
	
	public VirtualFileSystemManagedRepository(FileObject root){
		this.root = root;
	}

	@Override
	public boolean isReadable() {
		try {
			return root.isReadable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isWriteable() {
		try {
			return root.isWriteable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}
	
	public boolean isQueriable(){
		try {
			return root.getType().hasChildren();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		if (!this.isReadable()){
			throw new RepositoryNotRedableException(this.getClass().getName() + "(" +  this.root.getName().getBaseName() + ")");
		}

		try {
			return new VirtualFileSystemManagedFile(root.resolveFile("./" + filename)); // allways relative
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}
	
	@Override
	public boolean exists(String filename) throws ManagedIOException {
		try {
			FileObject fo = root.resolveFile("./" + filename);
			return fo.exists();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean delete(String filename) throws ManagedIOException {
		if (!this.isWriteable()){
			throw new RepositoryNotWritableException(this.getClass().getName() + "(" +  this.root.getName().getBaseName() + ")");
		}
		
		try {
			FileObject fo = root.resolveFile("./" + filename);
			return fo.delete();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean delete(ManagedFile file) throws ManagedIOException {
		
		return delete(file.getName());
	}



	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException,ManagedIOException {
		if (!this.isWriteable()){
			throw new RepositoryNotWritableException(this.getClass().getName() + "(" +  this.root.getName().getBaseName() + ")");
		}
		
		try {
			FileObject nfo = root.resolveFile(file.getName());
			if (!nfo.exists()){
				nfo.createFile();
				
				if (file instanceof VirtualFileSystemManagedFile){
					FileObject ofo = ((VirtualFileSystemManagedFile)file).file;
					
					FileUtil.copyContent(ofo, nfo);
				} else {
					IOUtils.copy(file.getContent().getInputStream(), nfo.getContent().getOutputStream());
				}
			}

		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		} catch (IOException ioe) {
			throw ManagedIOException.manage(ioe);
		}
	}

	@Override
	public void addFileChangelistener(FileChangeListener listener,ManagedFile file) {
		try {
			this.root.getFileSystem().addListener( root.resolveFile(file.getName()),new FileListenerAdapter(listener));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public void removeFileChangelistener(FileChangeListener listener,ManagedFile file) {
		try {
			this.root.getFileSystem().removeListener( root.resolveFile(file.getName()),new FileListenerAdapter(listener));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException {
		/*
		if (!this.isQueriable()){
			throw new RepositoryNotQueriableException(this.getClass().getName() + "(" +  this.root.getName().getBaseName() + ")");
		}
		*/
		
		FileObject[] files;
		try {
			files = this.root.getChildren();
			Collection<ManagedFile> mfiles = new ArrayList<ManagedFile>();

			for (FileObject fo : files){
				mfiles.add(new VirtualFileSystemManagedFile(fo));
			}

			return mfiles;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}


	}

	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter)
	throws ManagedIOException {
		if (!this.isQueriable()){
			throw new RepositoryNotQueriableException(this.getClass().getName() + "(" +  this.root.getName().getBaseName() + ")");
		}
		FileObject[] files;
		try {
			files = this.root.getChildren();
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




}
