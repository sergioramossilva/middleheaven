package org.middleheaven.storage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodIntrospector;
import org.middleheaven.core.reflection.ObjectInstrospector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.db.StoreQuerySession;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.ComposedMapKey;
import org.middleheaven.util.collections.DualMapKey;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractDataStorage implements DataStorage {

	private StorableModelReader reader;

	public AbstractDataStorage(StorableModelReader reader){
		this.reader = new CachedStorableModelReader(reader);
	}

	protected StorableModelReader reader() {
		return reader;
	}


	/* (non-Javadoc)
	 * @see org.middleheaven.storage.StorableModelResolver#resolveModel(java.util.Collection)
	 */
	public StorableEntityModel resolveModel(Collection<Storable> collection){
		if (collection.isEmpty()){
			return null;
		}

		Storable s = collection.iterator().next();
		return reader.read(s.getPersistableClass());
	}

	public void flatten(Storable p, Set<Storable> all){

		if (all.contains(p)){ // loop
			return;
		}

		// add only if it will cause any change
		if (!p.getStorableState().isNeutral()){
			all.add(p);
		}

		StorableEntityModel entityModel = this.reader.read(p.getPersistableClass());

		for (StorableFieldModel fieldModel : entityModel.fields()){
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

	@Override
	public Identity getIdentityFor(Object object) {
		if (object instanceof Storable){
			return ((Storable)object).getIdentity();
		}
		return null;
	}

	protected final Storable copy(Storable from, Storable to,StorableEntityModel model, StoreQuerySession session) {

		to.setIdentity(from.getIdentity());
		to.setStorableState(from.getStorableState());

		for (StorableFieldModel fm : model.fields()){
			if(fm.getDataType().isToManyReference()){
				// lookfor the other ones
				StorableEntityModel otherModel = reader.read(fm.getValueClass());

				StorableFieldModel frm = otherModel.fieldReferenceTo(to.getPersistableClass());

				if (frm !=null){
					Criteria<?> criteria = CriteriaBuilder.search(otherModel.getEntityClass())
					.and(frm.getLogicName().getName())
					.navigateTo(Introspector.of(to).getRealType())
					.and("identity").eq(to.getIdentity())
					.back()
					.all();

					Collection<?> all = this.createQuery(criteria , null).findAll();

					for (Object o : all){
						to.addFieldElement(fm,o);
					}

				}

			} else if(fm.getDataType().isToOneReference()) {
				Object obj = from.getFieldValue(fm);

				if(obj!=null){
					StorableEntityModel otherModel = reader.read(fm.getValueClass());

					// convert to identity
					Identity id = (Identity)TypeConvertions.convert(obj, otherModel.identityFieldModel().getValueClass());


					Storable o = session.get(otherModel.getEntityClass(), id);
					if (o == null){
						Criteria<?> criteria = CriteriaBuilder.search(otherModel.getEntityClass())
						.and("identity").eq(id)
						.all();

						o = (Storable) this.createQuery(criteria , null).find();
						session.put(o);
					}



					to.setFieldValue(fm, o);
				}

			} else {
				to.setFieldValue(fm,from.getFieldValue(fm));
				
				if (fm.isIdentity()){
					session.put(to);
				}
			}
		}
		
		return to;
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




}
