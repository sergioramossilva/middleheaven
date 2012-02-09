package org.middleheaven.domain.store;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;


/**
 * 
 */
public abstract class AbstractEntityInstanceStorage implements EntityInstanceStorage {

	
	private DomainStoreManager storeManager;
	
	private final Map<String , IdentityEditor> editors = new HashMap<String, IdentityEditor>();
	

	interface IdentityEditor {
		
		
		public Identity newInstance (Long seed);
	}
	
	
	public AbstractEntityInstanceStorage(){
		
		editors.put(IntegerIdentity.class.getName(), new IdentityEditor(){

			@Override
			public Identity newInstance(Long seed) {
				return IntegerIdentity.valueOf(seed.intValue());
			}
			
		} );
		
		editors.put(IntegerIdentity.class.getName(), new IdentityEditor(){

			@Override
			public Identity newInstance(Long seed) {
				return LongIdentity.valueOf(seed);
			}
			
		} );
	}
	
	@Override
	public void setStorableStateManager(DomainStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	protected DomainStoreManager getStoreManager(){
		return storeManager;
	}
	

	/**
	 * @return and {@link IdentityManager} based on the data store
	 */
	public IdentityManager getIdentityManager() {
		return new IdentityManager(){

			@Override
			public EntityInstance assignIdentity(EntityInstance instance) {
				
				EntityModel model = instance.getEntityModel();
				
				if (model.isIdentityAssigned()){
					// identity already was assigned 
					if (instance.getIdentity() == null){
						throw new IllegalArgumentException("Instance indentity is expected to be assigned before storage");
					}
				} else {
					IdentityEditor editor = editors.get(model.getIdentityType().getName());
					Sequence<Long> seedSequence = getSeedSequence(instance);
					
					Identity id = editor.newInstance(seedSequence.next().value());
					
					instance.setIdentity(id);
					
				}
				
				return instance;
			}
			
		};
	}
	
	protected abstract Sequence<Long> getSeedSequence(EntityInstance instance);
	
}
