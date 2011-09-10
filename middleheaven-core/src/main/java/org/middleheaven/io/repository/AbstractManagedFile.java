package org.middleheaven.io.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.middleheaven.io.IOUtils;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.collections.Walker;

/**
 * Default implementation of a {@link ManagedFile} usefull for extention.
 */
public abstract class AbstractManagedFile implements ManagedFile {


	private ManagedFileRepository repository;


	protected AbstractManagedFile(ManagedFileRepository repository){
		this.repository = repository;
	}

	public ManagedFileRepository getRepository(){
		return repository;
	}
	

	
	@Override
	public ManagedFile getParent() {
		return this.getRepository().retrive(this.getPath().getParent());
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
    public ManagedFile retrive(String path) throws ManagedIOException {
    	switch (this.getType()){
    	case FOLDER:
		case FILEFOLDER:
			return doRetriveFromFolder(path);
		case VIRTUAL:
			return this;
		case FILE:
		default:
			//no-op
			return null;
		}
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		// TODO Auto-generated method stub
		return null;
	}
    
    
	/**
	 * @param path
	 * @return
	 */
	protected abstract ManagedFile doRetriveFromFolder(String path);

	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		try {
			if (other.getType()==ManagedFileType.FILE){
				IOUtils.copy(this.getContent().getInputStream(), other.getContent().getOutputStream());
			} else {
				ManagedFile newFile = other.retrive(this.getPath());
				newFile.createFile();
				IOUtils.copy(this.getContent().getInputStream(), newFile.getContent().getOutputStream());
			}

		} catch (IOException ioe) {
			throw ManagedIOException.manage(ioe);
		}
	}
	

	@Override
	public boolean canRenameTo(String newName) {
		ManagedFile p = this.getParent();
		if ( p == null){
			return false;
		}
		return !p.retrive(p.getPath().resolve(newName)).exists();
	}
	
	@Override
	public void renameTo(String newName) {
		if (canRenameTo(newName)) {
			doRenameAndChangePath(this.getPath().resolveSibling(newName));
		}
	}
	
	/**
	 * @param resolveSibling
	 */
	protected abstract void doRenameAndChangePath(ManagedFilePath path);

	public String getText(){
		BufferedReader reader = null;
		try{

			reader = new BufferedReader(new InputStreamReader(this.getContent().getInputStream()));

			StringBuilder builder = new StringBuilder();

			String line;
			while ((line = reader.readLine()) !=null){
				builder.append(line).append("\n");
			}

			return builder.toString();


		} catch (Exception e){
			throw new RuntimeException(e);
		} finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	@Override
	public final ManagedFile createFile() {
		switch (this.getType()){
		case FILE:
		case FILEFOLDER:
			return this;
		case VIRTUAL:
			return doCreateFile();
		default:
			throw new UnsupportedOperationException("Cannot create file of type " + this.getType());
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final boolean contains(ManagedFile other) {
		switch (this.getType()){
		case FILEFOLDER:
		case FOLDER:
			return doContains(other);
		default:
			return false;
		}
	}
	
	protected abstract boolean doContains(ManagedFile other);
	
	@Override
	public final ManagedFile createFolder() {
		switch (this.getType()){
		case FOLDER:
		case FILEFOLDER:
			return this;
		case VIRTUAL:
			return doCreateFolder();
		default:
			throw new UnsupportedOperationException("Cannot create folder of type " + this.getType());
		}
	}
	



	protected abstract ManagedFile doCreateFile();
	protected abstract ManagedFile doCreateFolder();

	/**
	 * @return
	 */
	protected abstract Iterable<ManagedFile> children();

	@Override
	public void eachParent(Walker<ManagedFile> walker) {
		if(this.getParent()!=null){
			walker.doWith(this.getParent());
			this.getParent().eachParent(walker);
		}
	}

	@Override
	public void eachRecursive(Walker<ManagedFile> walker) {
		for (ManagedFile file  : this.children()){
			walker.doWith(file);
			file.eachRecursive(walker);
		}
	}


	@Override
	public void each(Walker<ManagedFile> walker) {
		for (ManagedFile file  : this.children()){
			walker.doWith(file);
		}
	}
}
