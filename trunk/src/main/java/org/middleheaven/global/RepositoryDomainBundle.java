/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.WatchableRepository;

/**
 * @author  Sergio M. M. Taborda 
 */
public class RepositoryDomainBundle extends LocalizationDomainBundle implements FileChangeListener{

    private ManagedFileRepository repository;
    private LocalizedMessagesFormatHandler fileFormat;

    
    private Map<String,LocalizedMessagesFormatHandler> files = new HashMap<String,LocalizedMessagesFormatHandler>();

    
    private Map<String,String> filesMapping = new HashMap<String,String>();
    
    public RepositoryDomainBundle(){  
        this.fileFormat = new PropertiesMessageFileFormat();
    }
    

    public void setRepository (ManagedFileRepository repository){
        this.repository = repository;
      
    }
    
    public void setFileFormat(LocalizedMessagesFormatHandler fileFormat){
        this.fileFormat = fileFormat;
    }
    
    @Override
    protected String findLabel(GlobalLabel label, Locale locale) throws MissingResourceException {
        
        String key = label.getDomain() + "_" + locale.getCountry() + "_" +  locale.getLanguage();
        LocalizedMessagesFormatHandler format; 
        synchronized(files){
            format = files.get(key);
        }
        
        if (format==null){
            StringBuilder filename = new StringBuilder(key + ".properties");
            
            while (!repository.exists(filename.toString() )){
                int pos = filename.lastIndexOf("_");
                if (pos<0){
                    throw new MissingResourceException(label.getLabel(), null ,label.getDomain());
                }
                filename.delete(pos, filename.length());
                filename.append(".properties");
            }
            ManagedFile file = repository.retrive(filename.toString());
            createWatcher(file);
            format = fileFormat.newFormatHandler(file.getInputStream());
            
            
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
        if (this.repository instanceof WatchableRepository){
            ((WatchableRepository)this.repository).addFileChangelistener(this, file);
        }
    }

	@Override
	public void onChange(FileChangeEvent event) {
		 LocalizedMessagesFormatHandler format = fileFormat.newFormatHandler(event.getFile().getInputStream());
	        synchronized(files){
	            files.put(filesMapping.get(event.getFile().getName()), format);
	        }
	}
}
