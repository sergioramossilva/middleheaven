package org.middleheaven.io.repository;

public interface ManagedFilePath {

/**
 *     Returns the base name of this file. The base name is the last element of the file name. For example the base name of /somefolder/somefile is somefile.

    The root file of a file system has an empty base name.

    Returns:
        The base name. Never returns null.


 * @return
 */
	public String getBaseName();
	
	/**
	 *     Returns the extension of this file name.

    Returns:
        The extension. Returns an empty string if the name has no extension.
	 * @return
	 */
	public String getExtension();
	

	/**
	 * Returns the depth of this file name, within its file system. The depth of the root of a file system is 0. The depth of any other file is 1 + the depth of its parent. 
	    
	 * @return
	 */
	int getDepth();

	    
	/**
	 *     Returns the absolute path of this file, within its file system. This path is normalised, so that . and .. elements have been removed. Also, the path only contains / as its separator character. The path always starts with /

    The root of a file system has / as its absolute path.

    Returns:
        The path. Never returns null.


	 * @return
	 */
	public String getPath();
	
	
	public ManagedFilePath getParent();
	
	
/**
 * The "requested" type is the one determined during resolving the name.
In this case the name is a FileType.FOLDER if it ends with an "/" else it will be a FileType.FILE
 * @return
 */
	public ManagedFileType getType();
	



}
