package org.middleheaven.storage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.transactions.TransactionService;


public class DomainStoreService implements EntityStoreService {

	private static final ThreadLocal<Map<String,EntityStore>> threadStore = new ThreadLocal<Map<String,EntityStore>>(); 
	
	private Map<String , Info > infos = new LinkedHashMap<String, Info> ();

	private static class Info {

		public DataStorage dataStorage;
		public DomainModel domainModel;

		public Info(DataStorage dataStorage, DomainModel domainModel) {
			this.dataStorage = dataStorage;
			this.domainModel = domainModel;
		}
		
	}
	
	public DomainStoreService(){
		
	}


	@Override
	public EntityStore getStore() {
		if(infos.isEmpty()){
			throw new IllegalStateException("No entity store is registered");
		}
		return getStore(infos.keySet().iterator().next());
	}


	@Override
	public EntityStore getStore(String name) {
		
		
		Map<String,EntityStore> stores = threadStore.get();
		if (stores==null){
			stores = new HashMap<String,EntityStore>();
			threadStore.set(stores);
		}
		
		EntityStore store = stores.get(name);
	
		if (store == null ){
			Info info = infos.get(name);
			if (info == null){
				throw new IllegalStateException("No entity store registered under the name " + name);
			}
			
			TransactionService ts = ServiceRegistry.getService(TransactionService.class);

			store = new SessionAwareEntityStore(new StorableStateManager(info.dataStorage, info.domainModel), info.dataStorage);
			ts.enlistResource((SessionAwareEntityStore)store);
			
			stores.put(name, store);
		}
		return store;
	}


	@Override
	public void register(String name, DataStorage dataStorage, DomainModel domainModel) {
		this.infos.put(name, new Info(dataStorage,domainModel));
		
	}


	@Override
	public void unRegister(String name) {
		this.infos.remove(name);
	}


	@Override
	public void unRegisterAll() {
		this.infos.clear();
	} 
	
	
}
