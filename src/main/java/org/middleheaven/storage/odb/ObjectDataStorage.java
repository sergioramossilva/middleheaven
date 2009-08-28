package org.middleheaven.storage.odb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ObjectInstrospector;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.WrapperProxy;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.storage.AbstractDataStorage;
import org.middleheaven.storage.DecoratorStorableEntityModel;
import org.middleheaven.storage.ExecutableQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.QueryExecuter;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.SimpleExecutableQuery;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.middleheaven.util.identity.Identity;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.nq.NativeQuery;

public class ObjectDataStorage extends AbstractDataStorage {

	private ManagedFile dataFile;
	private File file;

	public ObjectDataStorage(ManagedFile dataFile) {
		super(null);
		this.dataFile = dataFile;
		this.file = new File(dataFile.getURL().getFile());
	}

	private ODB getDataBase(){
		try {
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
	public Storable merge(Object obj){
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
			ObjectInstrospector<Object> introspector = Introspector.of(obj);
			p = introspector.newProxyInstance(new NeoDatisMethodHandler(this,obj),Storable.class, WrapperProxy.class );

			introspector.copyTo(p);
		}
		return p;
	}

	private static class NeoDatisMethodHandler implements ProxyHandler {

		private Object obj;
	
		private StorableState state = StorableState.FILLED;

		private ObjectDataStorage keeper;

		public NeoDatisMethodHandler(ObjectDataStorage keeper , Object obj) {
			this.obj = obj;
			this.keeper = keeper;
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

					return Introspector.of(self.getClass()).inspect().properties()
									.named(name).retrive().getValue(self);
					
				} else if (methodName.equals("setFieldValue")){
					StorableFieldModel model = (StorableFieldModel)args[0];
					String name = model.getHardName().getName();

					Introspector.of(self.getClass()).inspect().properties()
					.named(name).retrive().setValue(self,args[1]);
					
					return null;
				} else {
					return Introspector.of(this.getClass()).inspect().methods()
					.named(methodName).withParametersType(delegator.getInvoked().getParameterTypes()).retrive()
					.invoke(this, args);
				}
			} else {
				return delegator.invokeSuper(self, args);
			}
		}

		public Identity getIdentity(){
			return null; //OIDIdentity.valueOf(keeper.getDataBase().getObjectId(obj));
		}
		
		public String getName() {
			return obj.getClass().getSimpleName();
		}

		public Class<?> getPersistableClass() {
			return obj.getClass();
		}

		public StorableState getPersistableState() {
			return state;
		}

		public void setPersistableState(StorableState state) {
			this.state = state;
		}
	}



	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return new SimpleExecutableQuery<T>(criteria,null,neoDatisQueryExecuter);
	}
	
	private QueryExecuter neoDatisQueryExecuter = new QueryExecuter() {

		@Override
		public <T> Collection<T> execute(final ExecutableQuery<T> query) {
			final Criteria<T> criteria = query.getCriteria();
			final CriteriaFilter<T> filter = new CriteriaFilter<T>(criteria,query.getModel());
			
			ODB odb = getDataBase();
			try{
				IQuery iquery = new NativeQuery() {
			
				
					public boolean match(Object obj) {
						return filter.classify((T)obj).booleanValue();
					}

					@Override
					public Class getObjectType() {
						return criteria.getTargetClass();
					}
				};

				Objects result = odb.getObjects(iquery);

				Collection<T> all = new LinkedList<T>();

				final int START_AT = criteria.getStart();
				final int MAX_COUNT = criteria.getCount();
				int elapsed=0;
				while (elapsed<START_AT && result.hasNext()){ // will not loop if startAt==-1
					elapsed++;
					result.next();
				}
				
				if (MAX_COUNT>=0){
					while (result.hasNext() && all.size() < MAX_COUNT) { // add just maxCount elements
						all.add((T)merge(result.next()));
					}
				} else {
					while (result.hasNext()) {
						all.add((T)merge(result.next()));
					}
				}
				

				return all;
			}  catch (Exception e) {
				throw new StorageException(e);
			} finally {
				closeDataBase(odb);
			}
		}
		
	};



	@Override
	public void insert(Collection<Storable> obj) {

		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = Introspector.of(s).unproxy();
				OID id = odb.store(object);
				OIDIdentity identity = OIDIdentity.valueOf(id);
				s.setIdentity(identity);
			}
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}

	}

	@Override
	public void remove(Collection<Storable> obj) {
		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = Introspector.of(s).unproxy();
				odb.delete(object);
			}
		}  catch (Exception e) {
			throw new StorageException(e);
		} finally {
			closeDataBase(odb);
		}
	}

	@Override
	public void remove(Criteria<?> criteria) {
		// TODO implement StoreKeeper.remove

	}

	@Override
	public void update(Collection<Storable> obj) {
		ODB odb = getDataBase();
		try{
			for (Storable s : obj){
				Object object = Introspector.of(s).unproxy();
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
