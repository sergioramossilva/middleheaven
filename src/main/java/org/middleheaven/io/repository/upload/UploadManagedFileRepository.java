package org.middleheaven.io.repository.upload;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileContent;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.VirtualFolder;
import org.middleheaven.util.collections.EnhancedArrayList;
import org.middleheaven.util.collections.EnhancedCollection;

public class UploadManagedFileRepository extends AbstractManagedFile implements  ManagedFileRepository,QueryableRepository {


	public static ManagedFileRepository repositoryOf(HttpServletRequest request, Map<String, String> parameters) {
		return repositoryOf(request, parameters , null);
	}
	
	public static ManagedFileRepository repositoryOf(HttpServletRequest request, Map<String, String> parameters,ManagedFile temporary) {
		if (temporary != null && !(temporary.isWriteable() && temporary.getType().isOnlyFolder())){
			throw new ManagedIOException("Temporary location must be a folder");
		}

		UploadManagedFileRepository repository = new UploadManagedFileRepository();
		
		repository.factory = new DiskFileItemFactory();
		repository.factory.setSizeThreshold(1024*1024);
		if (temporary!=null){
			try {
				repository.factory.setRepository(new File(temporary.getURL().toURI()));
			} catch (URISyntaxException e) {
				// if cannot use temporary repository, then doesn't 
			}
		}

		final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {  
			repository.folders = new HashMap<String, VirtualFolder>();

			// Create a factory for disk-based file items

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(repository.factory);
			
			// Parse the request
			try {

				for (Iterator<?> it = upload.parseRequest(request).iterator();it.hasNext();){
					FileItem item = (FileItem) it.next();
					if (!item.isFormField()) {  
						// is a file
					
						VirtualFolder folder = new VirtualFolder(item.getFieldName(), repository);
						repository.folders.put(folder.getName(), folder);
						folder.add(new UploadManagedFile(item, repository));
						
					} else if (parameters!=null){
						parameters.put(item.getFieldName(), item.getString());
					}
				}

			} catch (FileUploadException e) {
				throw new ManagedIOException(e);
			}  
		} else {
			repository.folders = Collections.emptyMap();
		}

		return repository;
	}
	
	/**
	 * each file is put into a folder with the field name
	 */
	private Map<String, VirtualFolder> folders;
	private HttpServletRequest request;
	DiskFileItemFactory factory;
	private String name = "upload repository";
	
	private UploadManagedFileRepository(){}
	
	public void dispose(){
		// remove all files
		for (VirtualFolder folder :  folders.values()){
			folder.clear();
		}
		this.folders.clear();
	}
	
	@Override
	public boolean delete(String filename) throws ManagedIOException {
		return false;
	}

	@Override
	public boolean delete(ManagedFile file) throws ManagedIOException {
		return false;
	}
	@Override
	public boolean isQueriable() {
		return true;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public boolean exists(String filename) throws ManagedIOException {
		return folders.containsKey(filename);
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		return folders.get(filename);
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		throw new UnsupportedOperationException("Store is not supported");
	}

	@Override
	public EnhancedCollection<ManagedFile> children() throws ManagedIOException {
		return new EnhancedArrayList<ManagedFile> (folders.values()); 
	}

	@Override
	public boolean contains(ManagedFile other) {
		return folders.values().contains(other);
	}


	@Override
	public ManagedFile doCreateFile() {
		throw new UnsupportedOperationException("File creation is not supported");
	}

	@Override
	public ManagedFile doCreateFolder() {
		throw new UnsupportedOperationException("Folder creation is not supported");
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ManagedFileContent getContent() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManagedFile getParent() {
		return null;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FOLDER;
	}

	@Override
	public URL getURL() {
		try {
			return new URL(request.getRemoteAddr());
		} catch (MalformedURLException e) {
			throw new ManagedIOException(e);
		}
	}


	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public long getSize() throws ManagedIOException {
		long sum = 0;
		for (ManagedFile file  : this.folders.values()){
			sum += file.getSize();
		}
		return sum;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}



}
