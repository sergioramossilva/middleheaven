package org.middleheaven.io.repository.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.UnexistantManagedFile;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class DiskFileManagedRepository extends AbstractManagedRepository implements QueryableRepository{


	public static ManagedFileRepository repository(){
		return new DiskFileManagedRepository();
	}

	public static ManagedFileRepository repositoryFor(File file){
		return new DiskManagedFile(null,file);
	}

	private DiskFileManagedRepository(){

	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		File file = new File(filename);
		File[] roots = File.listRoots();
		for (File  root : roots){
			if (root.equals(file)){
				return new DiskManagedFile(null,root);
			}
		}
		// TODO iner search
		return new UnexistantManagedFile(null, filename);
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public EnhancedCollection<ManagedFile> children()throws ManagedIOException {
		File[] roots = File.listRoots();
		Collection<ManagedFile> result = new ArrayList<ManagedFile>(roots.length);

		for (File root : roots){
			result.add(new DiskManagedFile(null,root));
		}

		return CollectionUtils.enhance(result);
	}

	



}
