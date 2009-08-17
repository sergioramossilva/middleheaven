package org.middleheaven.storage;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodIntrospector;
import org.middleheaven.core.reflection.ObjectInstrospector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.validation.Consistencies;

public abstract class AbstractDataStorage implements DataStorage {

	private StorableModelReader reader;
	public AbstractDataStorage(StorableModelReader reader){
		this.reader = reader;
	}
	
	private final Map<String, StorableEntityModel> entityModels = new HashMap<String, StorableEntityModel>();
	private final Map<String, EntityModel> classEntities = new HashMap<String, EntityModel>();

	public final StorableEntityModel storableModelOf(EntityModel model){
		Consistencies.consistNotNull(model);
		
		StorableEntityModel sm = entityModels.get(model.getEntityName());
		if (sm==null){
			sm = reader.read(model);
			entityModels.put(model.getEntityName(),sm);
			classEntities.put(model.getEntityClass().getName(), model);
		}
		return sm;
	}

	
	@Override
	public Identity getIdentityFor(Object object) {
		if (object instanceof Storable){
			return ((Storable)object).getIdentity();
		}
		return null;
	}
	
	protected final void copy(Storable from, Storable to,StorableEntityModel model) {
		to.setIdentity(from.getIdentity());
		to.setStorableState(from.getStorableState());
		
		for (StorableFieldModel fm : model.fields()){
			if(fm.getDataType().isToManyReference()){
				// lookfor the other ones
				StorableEntityModel otherModel = storableModelOf(this.classEntities.get(fm.getValueClass().getName()));
				
				StorableFieldModel frm = otherModel.fieldReferenceTo(to.getPersistableClass());
				
				if (frm !=null){
					Criteria<?> criteria = CriteriaBuilder.search(otherModel.getEntityClass())
					.and(frm.getLogicName().getName())
					.is(to)
					.all();
					
					Collection<?> all = this.createQuery(criteria, otherModel , null).findAll();
					
					for (Object o : all){
						to.addFieldElement(fm,o);
					}
					
				}
				
			} else if(fm.getDataType().isToOneReference()) {
				Object obj = from.getFieldValue(fm);
				
				if(obj!=null){
					StorableEntityModel otherModel = storableModelOf(this.classEntities.get(fm.getValueClass().getName()));
					
					Criteria<?> criteria = CriteriaBuilder.search(otherModel.getEntityClass())
					.isEqual(obj)
					.all();
					
					Object o = this.createQuery(criteria, otherModel , null).find();
					
					to.setFieldValue(fm, o);
				}
				
			} else {
				to.setFieldValue(fm,from.getFieldValue(fm));
			}
		}
	}
	
	@Override
	public Storable merge(Object obj){
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
			ObjectInstrospector<Object> instrospector = Introspector.of(obj);
			p = instrospector.newProxyInstance(new StorableProxyHandler(obj.getClass()),  Storable.class);

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
					
					for (Object o : all){
						adder.invoke(null, copy, o);
					}
				}
				
			} else {
				fa.setValue(copy, value);
			}
		}

	}



	
}
