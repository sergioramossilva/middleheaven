package org.middleheaven.io.repository;

public enum ManagedFileType {

	 /**
	  * This type of file does not physically exist
	  */
	 VIRTUAL (false,false),
	 /**
	  * This type of file exists as a content holder that does not allow
	  * sub files
	  */
	 FILE (true,false),
	 /**
	  * This type of file exists as a container for other files and as
	  * no proper content
	  */
	 FOLDER (false,true),
	 /**
	  * This type of file exists as a container for other files and as
	  * content. the content is the description of the container files.
	  * ZIP and Jar files are examples of the FILEFOLDER type
	  */
	 FILEFOLDER (true,true);
	 
	
	 private boolean hasContent;
	 private boolean hasChildren;
	 
	private ManagedFileType(boolean hasContent, boolean hasChildren) {
		this.hasContent = hasContent;
		this.hasChildren = hasChildren;
	}
	
	public boolean isVirtual(){
		return this.equals(VIRTUAL);
	}
	
	public boolean isFolder(){
		return this.equals(FOLDER) || this.equals(FILEFOLDER) ;
	}
	
	
	public boolean hasContent(){
		return this.hasContent;
	}
	
	public boolean hasChildren (){
		return this.hasChildren;
	}

	public boolean isOnlyFolder() {
		return this.equals(FOLDER);
	}

	public boolean isFile() {
		return this.equals(FILE);
	}
}
