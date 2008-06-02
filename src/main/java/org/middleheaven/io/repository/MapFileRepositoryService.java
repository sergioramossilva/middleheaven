package org.middleheaven.io.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.middleheaven.io.repository.service.FileRepositoryService;

public class MapFileRepositoryService implements FileRepositoryService {

    private final Map<String,ManagedFile >  repositories = new HashMap<String,ManagedFile>();
    
    public ManagedFile getRepository(CharSequence name) {
        return repositories.get(name.toString());
    }

    public void registerRepository(CharSequence name, ManagedFile repository) {
        repositories.put(name.toString(), repository);
    }

    public void unregisterRepository(CharSequence name) {
        repositories.remove(name.toString());
    }

	@Override
	public Set<String> getRepositoriesNames() {
		return repositories.keySet();
	}

}
