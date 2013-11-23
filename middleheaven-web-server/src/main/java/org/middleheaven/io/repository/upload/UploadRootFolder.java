/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileType;

/**
 * 
 */
public class UploadRootFolder extends AbstractManagedFile {

	private ManagedFilePath path;
	private Map<ManagedFilePath , ManagedFile> files = new HashMap<ManagedFilePath , ManagedFile>();
	
	/**
	 * Constructor.
	 * @param repository
	 */
	protected UploadRootFolder(AbstractRequestFileRepository repository) {
		super(repository);
		path = new ArrayManagedFilePath(repository, "/");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath() {
		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWatchable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FOLDER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getURI() {
		return URI.create("uploaded://");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWriteable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StreamableContent doGetContent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String path) {
		return files.get(this.getPath().resolve(path));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRenameAndChangePath(ManagedFilePath path) {
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		return files.containsValue(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFile() {
		throw new IllegalStateException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFolder(ManagedFile parent) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterable<ManagedFile> childrenIterable() {
		return files.values();
	}

	/**
	 * @param uploadManagedFile
	 */
	public void add(ManagedFile uploadManagedFile) {
		files.put(uploadManagedFile.getPath(), uploadManagedFile);
	}

	/**
	 * 
	 */
	public void clear() {
		files.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		return files.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return files.get(this.getPath().resolve(path));
	}


}
