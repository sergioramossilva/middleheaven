package org.middleheaven.storage.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.collections.ComposedMapKey;
import org.middleheaven.util.collections.DualMapKey;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.validation.Consistencies;

public class StoreQuerySession {

	private final static ThreadLocal<StoreQuerySession> sessions = new ThreadLocal<StoreQuerySession>();
	
	public static StoreQuerySession getInstance(DataStorage dataStorage) {
		StoreQuerySession session =  sessions.get();
		if ( session == null){
			session = new StoreQuerySession(dataStorage);
			sessions.set(session);
		}
		return session;
	}
	
	
	private final Map<ComposedMapKey, Storable> cache = new HashMap<ComposedMapKey, Storable>();
	private final DataStorage dataStorage;
	private final AtomicInteger counter = new AtomicInteger();
	
	public StoreQuerySession(DataStorage dataStorage){
		this.dataStorage = dataStorage;
	}
	
	public void open() {
		counter.incrementAndGet();
	}

	public void close() {
		if (counter.decrementAndGet()==0){
			sessions.set(null);
		}
	}
	
	public void put(Storable to) {
		if (to.getIdentity() != null){
			cache.put(DualMapKey.of(to.getPersistableClass().getName(),to.getIdentity()), to);	
		}
		
	}
	
	public Storable get(Class<?> type, Identity identity){
		if (identity == null || type == null){
			return null;
		}
		return cache.get(DualMapKey.of(type.getName(),identity));

	}
	






}
