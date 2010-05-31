package org.middleheaven.io.repository;


public class SimpleManagedFilePath implements ManagedFilePath {

	
	private String baseName;
	private ManagedFilePath parentPath;

	public SimpleManagedFilePath(ManagedFilePath parentPath , String baseName){
		this.baseName = baseName;
		this.parentPath = parentPath;
	}
	
	public SimpleManagedFilePath(String baseName){
		this.baseName = baseName;
	}
	
	@Override
	public String getBaseName() {
		return baseName;
	}

	@Override
	public int getDepth() {
		return (parentPath == null ? 0 : parentPath.getDepth())+1;
	}

	@Override
	public String getExtension() {
		int pos = this.baseName.lastIndexOf('.');
		if (pos < 0){
			return "";
		}
		return this.baseName.substring(pos+1);
	}

	@Override
	public ManagedFilePath getParent() {
		return parentPath;
	}

	@Override
	public String getPath() {
		return parentPath == null ? "" : parentPath.getPath() + "/" + this.baseName;
	}

	@Override
	public ManagedFileType getType() {
		return this.baseName.endsWith("/") ? ManagedFileType.FOLDER : ManagedFileType.FILE;
	}

}
