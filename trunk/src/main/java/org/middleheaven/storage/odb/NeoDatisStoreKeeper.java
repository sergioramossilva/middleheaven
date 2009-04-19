package org.middleheaven.storage.odb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.reflection.WrapperProxy;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.storage.AbstractStoreKeeper;
import org.middleheaven.storage.DecoratorStorableEntityModel;
import org.middleheaven.storage.PersistableState;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.nq.NativeQuery;

public class NeoDatisStoreKeeper extends AbstractStoreKeeper{

	private ManagedFile dataFile;

	public NeoDatisStoreKeeper(ManagedFile dataFile) {
		super(new StorableModelReader(){

			@Override
			public StorableEntityModel read(EntityModel model) {
				return new DecoratorStorableEntityModel(model);
			}

		});
		this.dataFile = dataFile;
	}

	@Override
	public Storable merge(Object obj){
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
			p = ReflectionUtils.proxy(obj, new NeoDatisMethodHandler(obj), Storable.class, WrapperProxy.class);
			ReflectionUtils.copy(obj, p);
		}
		return p;
	}

	private static class NeoDatisMethodHandler implements ProxyHandler {

		private Object obj;
	
		private PersistableState state = PersistableState.FILLED;

		public NeoDatisMethodHandler(Object obj) {
			this.obj = obj;
		}

		@Override
		public Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {
			String methodName = delegator.getName();
			if ("getWrappedObject".equals(methodName)){
				return obj;
			} else if (!delegator.hasSuper()){
				if (methodName.equals("getFieldValue")){
					StorableFieldModel model = (StorableFieldModel)args[0];
					String name = model.getHardName().getName();

					return ReflectionUtils.getPropertyAccessor(self.getClass(),name).getValue(self);
				} else if (methodName.equals("setFieldValue")){
					StorableFieldModel model = (StorableFieldModel)args[0];
					String name = model.getHardName().getName();

					ReflectionUtils.getPropertyAccessor(this.obj.getClass(),name).setValue(self,args[1]);
					return null;
				} else {
					return ReflectionUtils.getMethod(this.getClass(), methodName, delegator.getInvoked().getParameterTypes()).invoke(this, args);
				}
			} else {
				return delegator.invokeSuper(self, args);
			}
		}

		public String getName() {
			return obj.getClass().getSimpleName();
		}

		public Class<?> getPersistableClass() {
			return obj.getClass();
		}


		public PersistableState getPersistableState() {
			return state;
		}

		public void setPersistableState(PersistableState state) {
			this.state = state;
		}
	}

	private ODB getDataBase(){
		try {
			File file = new File(dataFile.getURL().getFile());
			return ODBFactory.open(file.getAbsolutePath());
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	private void closeDataBase(ODB odb){
		try {
			odb.close();
		} catch (IOException e) {
			throw new StorageException(ManagedIOException.manage(e));
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {
		return new NeoDatisQuery<T>(criteria,model);
	}

	private class NeoDatisQuery<I> implements Query<I> {

		private Criteria<I> criteria;
		private StorableEntityModel model;

		public NeoDatisQuery(Criteria<I> criteria,StorableEntityModel model ){
			this.criteria = criteria;
			this.model= model;
		}

		@Override
		public long count() {
			return list().size();
		}

		@Override
		public I find() {
			if (list().isEmpty()){
				return null;
			}
			return list().iterator().next();
		}

		@Override
		public boolean isEmpty() {
			return list().isEmpty();
		}

		@Override
		public Collection<I> list() {
			return execute(criteria,model);
		}

	}

	private <T> Collection<T> execute (final Criteria<T> criteria,StorableEntityModel model){
		final CriteriaFilter<T> filter = new CriteriaFilter<T>(criteria,model);
	
		ODB odb = getDataBase();
		try{
			IQuery query = new NativeQuery() {
		
			
				public boolean match(Object obj) {
					return filter.classify((T)obj).booleanValue();
				}

				@Override
				public Class getObjectType() {
					return criteria.getTargetClass();
				}
			};

			Objects result = odb.getObjects(query);

			Collection<T> all = new LinkedList<T>();

			while (result.hasNext()) {
				all.add((T)merge(result.next()));
			}

			return all;
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}
	}

	@Override
	public void insert(Collection<Storable> obj, StorableEntityModel model) {

		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = ReflectionUtils.unproxy(s);
				OID id = odb.store(object);
				OIDIdentity identity = new OIDIdentity(id);
				s.setIdentity(identity);
			}
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}

	}

	@Override
	public void remove(Collection<Storable> obj, StorableEntityModel model) {
		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = ReflectionUtils.unproxy(s);
				odb.delete(object);
			}
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}
	}

	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		// TODO implement StoreKeeper.remove

	}

	@Override
	public void update(Collection<Storable> obj, StorableEntityModel model) {
		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = ReflectionUtils.unproxy(s);
				odb.store(object);
			}
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}
	}


	@Override
	public Storable assignIdentity(Storable storable) {
		return storable;
	}

}
