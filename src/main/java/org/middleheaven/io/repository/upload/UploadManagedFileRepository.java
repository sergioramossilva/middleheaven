package org.middleheaven.io.repository.upload;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileContent;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.QueryableRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;

public class UploadManagedFileRepository extends AbstractManagedFile implements  ManagedFileRepository,QueryableRepository {


	private Map<String, UploadManagedFile> files;
	private HttpServletRequest request;
	private DiskFileItemFactory factory;
	
	public UploadManagedFileRepository (HttpServletRequest request){
		this(request,null);
	}
	
	public UploadManagedFileRepository (HttpServletRequest request,ManagedFile temporary ){
		if (temporary != null && !(temporary.isWriteable() && temporary.getType().isOnlyFolder())){
			throw new ManagedIOException("Temporary location must be a folder");
		}
		this.request = request;
		factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*1024);
		if (temporary!=null){
			try {
				factory.setRepository(new File(temporary.getURL().toURI()));
			} catch (URISyntaxException e) {
				// if cannot use temporary repository, then doesn't 
			}
		}
		
	}
	
	/// if request was not read , read it now.
	private synchronized  void init()throws ManagedIOException{

		if (files==null){
			

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {  
				files = new TreeMap<String, UploadManagedFile>();

				// Create a factory for disk-based file items
				
				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				try {
					List<FileItem> items = upload.parseRequest(request);

					for ( FileItem item : items){
						if (!item.isFormField()) {  
							// is a file
							UploadManagedFile file = new UploadManagedFile(item, this);
							files.put(file.getName(), file);
						}  
					}

				} catch (FileUploadException e) {
					throw new ManagedIOException(e);
				}  
			} else {
				files = Collections.emptyMap();
			}
		}
	}
	@Override
	public ManagedFile create(String filename) throws ManagedIOException {
		throw new UnsupportedOperationException("Not supported");
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
		init();
		return files.containsKey(filename);
	}

	@Override
	public ManagedFile retrive(String filename) throws ManagedIOException {
		init();
		return files.get(filename);
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		throw new UnsupportedOperationException("Store is not supported");
	}

	@Override
	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException {
		init();
		return files.values();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException {

		init();
		Collection<ManagedFile> mfiles = new LinkedList<ManagedFile>();

		for (ManagedFile file : files.values()){
			if (filter.classify(file)){
				mfiles.add(file);
			}
		}

		return mfiles;

	}

	@Override
	public boolean contains(ManagedFile other) {
		return files.values().contains(other);
	}


	@Override
	public void createFile() {
		throw new UnsupportedOperationException("File creation is not supported");
	}

	@Override
	public void createFolder() {
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
		return null;
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
	public ManagedFile resolveFile(String filepath) {
		return this.retrive(filepath);
	}

}
