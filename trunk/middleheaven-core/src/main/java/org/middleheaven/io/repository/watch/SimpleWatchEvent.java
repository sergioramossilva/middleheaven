package org.middleheaven.io.repository.watch;

import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
public class SimpleWatchEvent implements WatchEvent{

	private final ManagedFile file;
	private final Kind kind;
	
	/**
	 * 
	 * Constructor.
	 * @param file the managed file.
	 */
    public SimpleWatchEvent(ManagedFile file, Kind kind){
		this.file = file;
		this.kind = kind;
	}
    
	public final ManagedFile getFile(){
		return file;
	}

	@Override
	public Kind kind() {
		return kind;
	}

	@Override
	public int count() {
		return 1;
	}
}
