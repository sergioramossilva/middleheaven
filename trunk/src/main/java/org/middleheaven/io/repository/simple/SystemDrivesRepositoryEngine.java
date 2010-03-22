package org.middleheaven.io.repository.simple;


import java.io.File;
import java.net.URI;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileResolver;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.io.repository.RepositoryEngine;
import org.middleheaven.io.repository.UnsupportedSchemeException;

public class SystemDrivesRepositoryEngine implements RepositoryEngine {

	private SystemDrivesEngineManagedFileResolver resolver = new SystemDrivesEngineManagedFileResolver();


	@Override
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException {
		return resolver;
	}

	
	private class SystemDrivesEngineManagedFileResolver implements ManagedFileResolver{

		DiskFileManagedRepository diskFileManagedRepository;
		
		private SystemDrivesEngineManagedFileResolver(){
			diskFileManagedRepository  = DiskFileManagedRepository.repository();
		}

		@Override
		public ManagedFile resolveURI(URI uri) {
			if (uri.getScheme().equals("file")){
				return diskFileManagedRepository.resolveURI(uri);
			} else {
				throw new UnsupportedSchemeException(uri.getScheme() + " is not supported by " + SystemDrivesRepositoryEngine.class.getName());
			}
			
		}
		
	}

}
