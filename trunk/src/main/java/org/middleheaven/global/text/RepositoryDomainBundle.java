
package org.middleheaven.global.text;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.WatchableContainer;

/**
 * Uses a key-value property file named {@code domain_language_country.properties} as source for 
 * {@code GlobalLabel}'s localization. 
 * If this file isn't found the file {@code domain_language.properties} is used instead. 
 * If this file also ins't found the file {@code domain.properties}.
 * IF no domain file is found an exception is throwned.
 * 
 */
public class RepositoryDomainBundle extends LocalizationDomainBundle implements FileChangeListener{

	private ManagedFile repository;
	private LocalizedMessagesFormatHandler fileFormat;


	private Map<String,LocalizedMessagesFormatHandler> files = new HashMap<String,LocalizedMessagesFormatHandler>();


	private Map<String,String> filesMapping = new HashMap<String,String>();
	private MessageFormat notFoundFormat = new MessageFormat("??{0}:{1}??");

	public RepositoryDomainBundle(){  
		this.fileFormat = new PropertiesMessageFileFormat();
	}

	/**
	 * A {@code MessageFormat} compatible message format to use when the label is not found on any bundle.
	 * The default is the pattern
	 * <pre>
	 * ??{0}:{1}??   
	 * </pre>
	 * The first argument is the label's domain and the second is the label's key.
	 * @param pattern
	 */
	public void setNotFoundPattern(String pattern){
		notFoundFormat= new MessageFormat(pattern);
	}

	public void setRepository (ManagedFile repository){
		this.repository = repository;

	}

	public void setFileFormat(LocalizedMessagesFormatHandler fileFormat){
		this.fileFormat = fileFormat;
	}

	@Override
	protected String findLabel(GlobalLabel label, Locale locale) throws MissingResourceException {

		String key = label.getDomain()  + "_" +  locale.getLanguage()+ "_" + locale.getCountry();
		LocalizedMessagesFormatHandler format; 
		synchronized(files){
			format = files.get(key);
		}

		if (format==null){


			StringBuilder filename = new StringBuilder(key + ".properties");

			while (!repository.resolveFile(filename.toString()).exists()){
				int pos = filename.lastIndexOf("_");
				if (pos<0){
					final Object[] args = {label.getDomain(),label.getLabel()};
					return notFoundFormat.format(args);
				}
				filename.delete(pos, filename.length());
				filename.append(".properties");
			}
			ManagedFile file = repository.resolveFile(filename.toString());
			createWatcher(file);
			format = fileFormat.newFormatHandler(file.getContent().getInputStream());


			files.put(key, format);
			filesMapping.put(file.getName(), key);
		}
		return format.findLabel(label);

	}

	/**
	 * Registers as a <code>FileChangeObserver<code> for the passed file.
	 * this way when/if the base file is changed changes will automaticly be propagated
	 * This can only be used if the repository is a <code>WatchableRepository<code>
	 * @param file
	 */
	private void createWatcher(ManagedFile file ){
		if (this.repository instanceof WatchableContainer){
			((WatchableContainer)this.repository).addFileChangelistener(this, file);
		}
	}

	@Override
	public void onChange(FileChangeEvent event) {
		LocalizedMessagesFormatHandler format = fileFormat.newFormatHandler(event.getFile().getContent().getInputStream());
		synchronized(files){
			files.put(filesMapping.get(event.getFile().getName()), format);
		}
	}
}
