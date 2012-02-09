package org.middleheaven.domain.store;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.transactions.TransactionService;

/**
 * {@link DomainStoreService} implementations that uses a simple Map to map names with configuration.
 */
public class HashDomainStoreService implements DomainStoreService {

	private static final ThreadLocal<Map<String,DomainStore>> threadStore = new ThreadLocal<Map<String,DomainStore>>(); 
	
	private final Map<String , Info > infos = new HashMap<String, Info> ();
	private String firstEntityName;
	
	/**
	 * Auxiliary structure.
	 */
	private static class Info {

		private EntityInstanceStorage dataStorage;
		private DomainModel domainModel;
		private IdentityManager identityManager;
		
		public Info (IdentityManager identityManager, EntityInstanceStorage dataStorage, DomainModel domainModel) {
			this.identityManager = identityManager;
			this.dataStorage = dataStorage;
			this.domainModel = domainModel;
		}
		
	}
	
	/**
	 * 
	 * Constructor.
	 */
	public HashDomainStoreService(){
		
	}

	@Override
	public DomainStore getStore() {
		if(firstEntityName == null){
			throw new IllegalStateException("No entity store is registered.Please register an EntityStore first.");
		}
		return getStore(firstEntityName);
	}


	@Override
	public DomainStore getStore(String name) {
		
		Map<String,DomainStore> stores = threadStore.get();
		if (stores==null){
			stores = new HashMap<String,DomainStore>();
			threadStore.set(stores);
		}
		
		DomainStore store = stores.get(name);
	
		if (store == null ){
			Info info = infos.get(name);
			if (info == null){
				throw new IllegalStateException("No entity store registered under the name " + name);
			}
			
			TransactionService ts = ServiceRegistry.getService(TransactionService.class);

			store = new SessionAwareDomainStore(info.identityManager, info.dataStorage, info.domainModel);
			ts.enlistResource((SessionAwareDomainStore)store);
			
			stores.put(name, store);
		}
		return store;
	}


	@Override
	public void registerStore(String name, IdentityManager identityManager, EntityInstanceStorage dataStorage, DomainModel domainModel) {
		if(infos.isEmpty()){
			this.firstEntityName = name;
		}
		this.infos.put(name, new Info(identityManager, dataStorage,domainModel));
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
