/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.util.sequence.persistent;


import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.util.sequence.StatePersistentSequence;
import org.middleheaven.util.sequence.service.SequenceStorageService;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class StorageServiceStatePersistentSequence<T> implements StatePersistentSequence<T> {

    String name;
    
    protected StorageServiceStatePersistentSequence(String name){
        this.name = name;
        SequenceStorageService service = ServiceRegistry.getService(SequenceStorageService.class);
        inicializeLast(service.retriveLastValue(this));
      
    }
    
    protected abstract void inicializeLast(String value);
        
    
    public String getName() {
        return name;
    }


    public void persist() {
        SequenceStorageService service = ServiceRegistry.getService(SequenceStorageService.class);
        service.store(this);
       
    }

}
