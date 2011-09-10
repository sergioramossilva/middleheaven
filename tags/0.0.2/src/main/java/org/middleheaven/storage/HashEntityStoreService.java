package org.middleheaven.storage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.model.domain.DomainModel;
import org.middleheaven.transactions.TransactionService;

public class HashEntityStoreService implements EntityStoreService {

	private static final ThreadLocal<Map<String,EntityStore>> threadStore = new ThreadLocal<Map<String,EntityStore>>(); 
	
	private final Map<String , Info > infos = new HashMap<String, Info> ();
	private String firstEntityName;
	
	private static class Info {

		public DataStorage dataStorage;
		public DomainModel domainModel;

		public Info(DataStorage dataStorage, DomainModel domainModel) {
			this.dataStorage = dataStorage;
			this.domainModel = domainModel;
		}
		
	}
	
	public HashEntityStoreService(){
		
	}

	@Override
	public EntityStore getStore() {
		if(firstEntityName == null){
			throw new IllegalStateException("No entity store is registered.Please register an EntityStore first.");
		}
		return getStore(firstEntityName);
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

			store = new SessionAwareEntityStore(new StorableStateManager(info.dataStorage, info.domainModel));
			ts.enlistResource((SessionAwareEntityStore)store);
			
			stores.put(name, store);
		}
		return store;
	}


	@Override
	public void registerStore(String name, DataStorage dataStorage, DomainModel domainModel) {
		if(infos.isEmpty()){
			this.firstEntityName = name;
		}
		this.infos.put(name, new Info(dataStorage,domainModel));
	}

	@Override
	public void unRegisterStore(String name) {
		this.infos.remove(name);
	}

	@Override
	public void unRegisterAll() {
		this.infos.clear();
	} 
	
	
}
