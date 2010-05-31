package org.middleheaven.storage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodIntrospector;
import org.middleheaven.core.reflection.ObjectInstrospector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;
import org.middleheaven.util.criteria.Criteria;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.identity.Identity;


/**
 * Comon storable state controler
 */
public final class StorableStateManager {

	private final DataStorage storage;
	private final DomainModel domainModel;

	public StorableStateManager (DataStorage storage, DomainModel model){
		this.storage = storage;
		this.storage.setStorableStateManager(this);
		this.domainModel = model;
	}
	
	public <T> Query<T> createQuery(final EntityCriteria<T> criteria, final ReadStrategy strategy, final StorageUnit unit) {
		
		return new SimpleExecutableQuery<T>(criteria , null, new QueryExecuter (){

			@Override
			public <E> Collection<E> execute(ExecutableQuery<E> query) {
				
				Collection all = storage.createQuery(criteria , strategy).fetchAll();
				
				all = unit.filter(all);
				
				return all;
			}
			
		});

	}
	
	/**
	 * Wraps the object with a {@code Storable} interface.
	 * If the object is already a {@code Storable}, return the object as it is.
	 * @param obj
	 * @return
	 */
	public Storable merge(Object obj){
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
			ObjectInstrospector<Object> instrospector = Introspector.of(obj);
			EntityModel model = this.domainModel.getEntityModelFor(obj.getClass());
			p = instrospector.newProxyInstance(new StorableProxyHandler(obj.getClass(),model),  Storable.class);

			copyTo(obj,p);

		}
		return p;
	}

	public void copyTo(Object original, Storable copy ){

		ClassIntrospector<? extends Object> introspector = Introspector.of(original.getClass());
		EnhancedCollection<PropertyAccessor> properties = introspector.inspect().properties().retriveAll();
		for (PropertyAccessor fa : properties){
			Object value = fa.getValue(original);
			if ( value instanceof Collection){
				Collection all = (Collection) value;

				if (!all.isEmpty()){
					Object first = all.iterator().next();
					MethodIntrospector adder = Introspector.of(introspector.inspect().methods()
							.withParametersType(new Class[]{first.getClass()})
							.notInheritFromObject()
							.match(new BooleanClassifier<Method>(){

								@Override
								public Boolean classify(Method obj) {
									return obj.getName().startsWith("add");
								}

							})
							.retrive());

					if (adder == null){
						throw new RuntimeException("Add method for " + first.getClass().getName() + "  not found in " + introspector.getName());
					}
					
					for (Object o : all){
						adder.invoke(null, copy, o);
					}
				}

			} else {
				fa.setValue(copy, value);
			}
		}

	}

	public final Identity getIdentityFor(Object object) {
		if (object instanceof Storable) {
			return ((Storable) object).getIdentity();
		}
		return null;
	}
	
	private StoreAction assignAction(Storable p){
		switch (p.getStorableState()) {
		case DELETED:
			if (p.getIdentity() == null) {
				return null;
			}
			return new DeleteAction(p);
		case FILLED:
		case EDITED:
			if (p.getIdentity() == null) {
				storage.assignIdentity(p);
				return new InsertAction(p);
			} else {
				// update
				return new UpdateAction(p);
			}
		case BLANK:
		case RETRIVED:
			// no-op
			return null;
		default:
			throw new IllegalStateException(p.getStorableState() + " is unkown");
		}
	}
	
	
	 
	public void flatten(Storable p, Set<Storable> all){

		if (all.contains(p)){ // loop
			return;
		}

		// add only if it will cause any change
		if (!p.getStorableState().isNeutral()){
			all.add(p);
		}

		
		EntityModel entityModel = p.getEntityModel();

		for (EntityFieldModel fieldModel : entityModel.fields()){
			if(fieldModel.getDataType().isToOneReference()){
				Storable merged = this.merge(p.getFieldValue(fieldModel));
				if(merged != null){
					p.setFieldValue(fieldModel, merged);
					flatten(merged, all);
				}
			} else if (fieldModel.getDataType().isToManyReference()){
			
				Collection<?> allRefereed = new ArrayList((Collection<?>)p.getFieldValue(fieldModel));
				for (Object o : allRefereed){
					if(o !=null){
						Storable merged = this.merge(o);
						p.removeFieldElement(fieldModel, o);
						p.addFieldElement(fieldModel, merged);
						flatten(merged, all);
					}
				}
			}
		}

	}

	
	public <T> void remove(T obj, StorageUnit unit) {
		Storable p = this.merge(obj);
		if (p.getIdentity()!=null) {
			p.setStorableState(StorableState.DELETED);
			unit.addAction(assignAction(p));
		}
		// else
		// not identified
		// do nothing as this obj is not in the store

	}

	public <T> T store(T obj, final StorageUnit unit) {

		Storable p = this.merge(obj);
		final Set<Storable> all = new HashSet<Storable>();
		
		flatten(p,all);


		CollectionUtils.enhance(all).collect(new Classifier<StoreAction, Storable>(){

			@Override
			public StoreAction classify(Storable s) {
				return assignAction(s);
			}

		}).each(new Walker<StoreAction>(){

			@Override
			public void doWith(StoreAction action) {
				unit.addAction(action);
			}

		});

		return (T)p;
	}

	public void commit(StorageUnit unit) {
		unit.commitTo(storage);
	}

	public void roolback(StorageUnit unit) {
		unit.roolback();
	}
}
