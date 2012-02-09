package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.core.reflection.metaclass.ListenableMetaBean;
import org.middleheaven.core.reflection.metaclass.MetaBean;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.TransformedEnumerable;
import org.middleheaven.util.identity.Identity;

/**
 * {@link EntityInstance} implementation backed by a Map.
 */
public final class MetaBeanEntityInstance implements EntityInstance {

	private final MetaBean bean;
	private final EntityModel model;
	
	private StorableState state = StorableState.FILLED;
	
	/**
	 * 
	 * Constructor.
	 * @param model the entity model.
	 * @param metaBean the bean meta class.
	 */
	public MetaBeanEntityInstance (EntityModel model, MetaBean metaBean){
		this.model = model;
		this.bean = new ListenableMetaBean(metaBean){
			protected void onModified(String name, Object value){
				state = state.edit();
			}
		};
	}
	
	@Override
	public StorableState getStorableState() {
		return state;
	}

	@Override
	public void setStorableState(StorableState state) {
		this.state = state;
	}
	
	@Override
	public Identity getIdentity() {
		return (Identity)this.bean.get(model.identityFieldModel().getName().getName());
	}
	
	@Override
	public void setIdentity(Identity id) {
		this.bean.set(model.identityFieldModel().getName().getName(), id);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public void copyFrom(EntityInstance other){
		
		for (EntityInstanceField field : other.getFields()){
			this.bean.set(field.getModel().getName().getName(), field.getValue());
		}
		
	}

	@Override
	public EntityModel getEntityModel() {
		return model;
	}

	@Override
	public EntityInstanceField getField(final String name) {
		return new EntityInstanceField (){

			@Override
			public EntityFieldModel getModel() {
				return model.fieldModel(QualifiedName.qualify(name));
			}

			@Override
			public Object getValue() {
				return bean.get(name);
			}

			@Override
			public void setValue(Object value) {
				bean.set(name , value);
			}

			@Override
			public void add(Object item) {
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) getValue();
				
				if ( collection == null){
					collection = new LinkedList<Object>();
				}
				
				collection.add(item);
			}

			@Override
			public void remove(Object item) {
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) getValue();
				
				if ( collection != null){
					collection.remove(item);
				}
			}
			
		};
	}

	@Override
	public Enumerable<EntityInstanceField> getFields() {
		return TransformedEnumerable.<EntityFieldModel,EntityInstanceField >transform(
				model.fields(),
				new Classifier<EntityInstanceField,EntityFieldModel>(){

					@Override
					public EntityInstanceField classify(EntityFieldModel fieldModel) {
						return getField(fieldModel.getName().getName());
					}
					
				}
		);
	}


}
