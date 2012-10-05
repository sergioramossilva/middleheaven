
package org.middleheaven.global.text;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;

/**
 * Uses a key-value property file named {@code domain_language_country.properties} as source for 
 * {@code GlobalLabel}'s localization. 
 * If this file isn't found the file {@code domain_language.properties} is used instead. 
 * If this file also ins't found the file {@code domain.properties}.
 * IF no domain file is found an exception is throwned.
 * 
 */
public class RepositoryDomainBundle extends LocalizationDomainBundle {

	private ManagedFile repository;
	private LocalizedMessagesFormatHandler fileFormat;


	private Map<String,LocalizedMessagesFormatHandler> files = new HashMap<String,LocalizedMessagesFormatHandler>();


	private final Map<String,String> filesMapping = new HashMap<String,String>();
	private MessageFormat notFoundFormat = new MessageFormat("??{0}:{1}??");
	private final FileWatchChannelProcessor fileWatchChannelProcessor;
	
	public RepositoryDomainBundle(){  
		this.fileFormat = new PropertiesMessageFileFormat();
		fileWatchChannelProcessor = new FileWatchChannelProcessor(new FileChangeStrategy() {
			
			@Override
			public void onChange(WatchEvent event) {
				LocalizedMessagesFormatHandler format = fileFormat.newFormatHandler(event.getFile().getContent().getInputStream());
				synchronized(files){
					files.put(filesMapping.get(event.getFile().getPath().getFileNameWithoutExtension()), format);
				}
			}
		});
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
	protected String findLabel(TextLocalizable label, Culture culture) throws MissingResourceException {

		String key = label.getDomain()  + "_" +  culture.getLanguage()+ "_" + culture.getCountry();
		LocalizedMessagesFormatHandler format; 
		synchronized(files){
			format = files.get(key);
		}

		if (format==null){


			StringBuilder filename = new StringBuilder(key + ".properties");

			while (!repository.retrive(filename.toString()).exists()){
				int pos = filename.lastIndexOf("_");
				if (pos<0){
					final Object[] args = {label.getDomain(),label.getMessageKey()};
					return notFoundFormat.format(args);
				}
				filename.delete(pos, filename.length());
				filename.append(".properties");
			}
			ManagedFile file = repository.retrive(filename.toString());
			createWatcher(file);
			format = fileFormat.newFormatHandler(file.getContent().getInputStream());


			files.put(key, format);
			filesMapping.put(file.getPath().getFileNameWithoutExtension(), key);
		}
		return format.findLabel(label);

	}

	/**
	 * Registers as a <code>FileChangeObserver<code> for the passed file.
	 * this way when/if the base file is changed changes will automaticly be propagated
	 * This can only be used if the file is watchable.
	 * @param file
	 */
	private void createWatcher(ManagedFile file ){
		if (file.isWatchable()){
			
			WatchEventChannel channel = file.register(
					file.getRepository().getWatchService(),
					StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
			fileWatchChannelProcessor.add(channel);		

		}
	}

}
