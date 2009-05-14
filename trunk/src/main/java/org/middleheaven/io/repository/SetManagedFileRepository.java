package org.middleheaven.io.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.EnhancedCollection;

public class SetManagedFileRepository extends AbstractManagedRepository implements QueryableRepository {

	Map<String , ManagedFile> files = new HashMap<String,ManagedFile>();

	
	public static SetManagedFileRepository repository(){
		return new SetManagedFileRepository();
	}
	
	private SetManagedFileRepository(){

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
	public ManagedFile retrive(String filename) throws ManagedIOException {
		ManagedFile file = files.get(filename);
		if (file==null){
			// use Media file for compatibility with any file that would be stored
			file = new BufferedMediaVirtualFile(filename);
			this.files.put(filename, file);
		}
		return file;
	}

	@Override
	public EnhancedCollection<ManagedFile> listFiles() throws ManagedIOException {
		return this.files.values();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(BooleanClassifier<ManagedFile> filter) throws ManagedIOException {
		
		Set<ManagedFile> result = new HashSet<ManagedFile>();
		
		for (ManagedFile file : this.files.values()){
			if (filter.classify(file)){
				result.add(file);
			}
		}
		return result;
	}

}
