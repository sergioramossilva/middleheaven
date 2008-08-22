package org.middleheaven.io.repository;

import java.io.IOException;

import org.middleheaven.io.IOUtils;
import org.middleheaven.io.ManagedIOException;

public abstract class AbstractManagedFile implements ManagedFile{

	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		try {
			if (other.getType()==ManagedFileType.FILE){
				IOUtils.copy(this.getContent().getInputStream(), other.getContent().getOutputStream());
			} else {
				ManagedFile newFile = other.resolveFile(this.getName());
				newFile.createFile();
				IOUtils.copy(this.getContent().getInputStream(), newFile.getContent().getOutputStream());
			}

		} catch (IOException ioe) {
			throw ManagedIOException.manage(ioe);
		}
	}
}
