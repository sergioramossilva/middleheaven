package org.middleheaven.storage;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ProxyUtils;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.UUIDIdentity;
import org.middleheaven.util.identity.UUIDIdentitySequence;
import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.SequenceToken;

public class DomainDataStorage implements DataStorage {

	StoreManager storeManager;
	StoreMetadataManager metadataService;
	
	public DomainDataStorage(StoreManager storeManager,StoreMetadataManager metadataService) {
		this.storeManager = storeManager;
		this.metadataService = metadataService;
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria) {
		return this.createQuery(criteria,ReadStrategy.none());
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return storeManager.createQuery(criteria, metadataService.getStorageModel(criteria.getTargetClass()) ,strategy);
	}
	
	@Override
	public <T> T store(T obj) {
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
		    p = ProxyUtils.decorate(obj, Storable.class, new PersistableMethodHandler(obj.getClass()));
		}
		doStore(p);
		return (T)p;
	}
	
	private final void doStore(Storable p){
		switch (p.getPersistableState()){
		case DELETED:
			doDelete(p);
			break;
		case EDITED:
			doUpdate(p);
			break;
		case FILLED:
			doInsert(p);
			break;
		case BLANK:
		case RETRIVED:
			// not-op
			break;
		default:
			throw new IllegalStateException(p.getPersistableState() + " is unkown");
		}
	}
	
	private Map<String, IdentitySequence> sequences = new TreeMap<String,IdentitySequence>();
	
	protected Identity nextID(Class<?> entityType) {
		Class<? extends Identity> identityType = metadataService.indentityTypeFor(entityType);
		if (UUIDIdentity.class.isAssignableFrom(identityType)){
			return new UUIDIdentitySequence().next().value();
		} else {
			
		}
		return nextID(identityType , metadataService.getStorageModel(entityType).logicNameForEntity());
	}

	protected <I extends Identity> I nextID(Class<I> identityType,String identifiableName) {
		if (identityType.equals(IntegerIdentity.class)){
			
			IdentitySequence<IntegerIdentity> idSequence = sequences.get(identifiableName);
			if (idSequence == null){
				Sequence<Long> dialectSequence = this.storeManager.getSequence(identifiableName); 
				idSequence = new DataBaseIntegerIdentitySequence(dialectSequence);
				sequences.put(identifiableName,idSequence);
			} 
			
			return identityType.cast(idSequence.next().value());

		} else {
			throw new StorageException("Identity of type " + identityType.getName() + " is not supported by " + this.getClass().getName());
		}
	}

	private static class DataBaseIntegerIdentitySequence implements IdentitySequence<IntegerIdentity> {

		Sequence<Long> baseSequence;
		
		public DataBaseIntegerIdentitySequence(Sequence<Long> baseSequence) {
			super();
			this.baseSequence = baseSequence;
		}

		@Override
		public SequenceToken<IntegerIdentity> next() {
			// TODO define LongIdentity
			return new DefaultToken<IntegerIdentity>(new IntegerIdentity(baseSequence.next().value().intValue()));
		}
		
		
	}
	
	private void doInsert(Storable p) {
		if (p.getKey()!=null){
			doUpdate(p);
		}
		
		// assign key
		p.setIdentity(this.storeManager.getSequence(p.getPersistableClass().getName()).next().value());
		
		this.storeManager.insert(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doUpdate(Storable p) {
		if (p.getIdentity()==null){
			doInsert(p);
		}
		
		this.storeManager.update(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doDelete(Storable p) {
		if (p.getIdentity()==null){
			return;
		}
		
		this.storeManager.remove(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.DELETED);
	}



	@Override
	public <T> void remove(T obj) {
		if (obj instanceof Storable){
			Storable p = (Storable)obj;
			p.setPersistableState(PersistableState.DELETED);
			doStore(p);
		} 
		// else 
		// not managed
		// do nothing as this obj is not in the store
		
	}

	@Override
	public <T> void remove(Criteria<T> criteria) {
		this.storeManager.remove(criteria, metadataService.getStorageModel(criteria.getTargetClass()));
	}

}
