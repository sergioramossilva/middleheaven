package org.middleheaven.io.repository.vfs;

import org.apache.commons.vfs.FileName;
import org.middleheaven.io.IOUtils;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileType;

public class VirtualFileManagedFilePath implements ManagedFilePath {

	private FileName name;

	public VirtualFileManagedFilePath(FileName name) {
		this.name = name;
	}

	@Override
	public String getBaseName() {
		return name.getBaseName();
	}

	@Override
	public int getDepth() {
		return name.getDepth();
	}

	@Override
	public String getExtension() {
		return name.getExtension();
	}

	@Override
	public ManagedFilePath getParent() {
		return new VirtualFileManagedFilePath(this.name.getParent());
	}

	@Override
	public String getPath() {
		return name.getPath();
	}

	@Override
	public ManagedFileType getType() {
		return this.name.getPath().endsWith("/") ? ManagedFileType.FOLDER : ManagedFileType.FILE;
	}

}
