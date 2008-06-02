package org.middleheaven.io.repository;

public class FileChangeEvent {

	private ManagedFile file;
    public FileChangeEvent(ManagedFile file){
		this.file = file;
	}
    
	public final ManagedFile getFile(){
		return file;
	}
}
