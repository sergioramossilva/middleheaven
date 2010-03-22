package org.middleheaven.io.repository.simple;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.FileNotFoundManagedException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileResolver;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.UnexistantManagedFile;
import org.middleheaven.io.repository.UnsupportedSchemeException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class DiskFileManagedRepository extends AbstractManagedRepository implements ManagedFileResolver , QueryableRepository{


	public static DiskFileManagedRepository repository(){
		return new DiskFileManagedRepository();
	}

	public static ManagedFileRepository repositoryFor(File file){
		return new DiskManagedFile(null,file);
	}

	private DiskFileManagedRepository(){

	}
	
	@Override
	public ManagedFile resolveURI(URI uri) throws UnsupportedSchemeException {
		if (uri.getScheme().equals("file")){
			return retriveFromFile(new File(uri));
		} else {
			throw new UnsupportedSchemeException(uri.getScheme() + " is not supported");
		}
	}
	
	protected boolean isDriveRoot(File file){

		return file.getParentFile() == null;
	}

	protected ManagedFile retriveFromFile(File file){
		if (isDriveRoot(file)){
			File[] roots = File.listRoots();
			for (File  root : roots){
				if (root.equals(file)){
					return new DiskManagedFile(null,root);
				}
			}
			throw new FileNotFoundManagedException(file.getAbsolutePath());
		} else {
			return new DiskManagedFile(null,file);
		}
	}
	
	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		return retriveFromFile(new File(filename));
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
