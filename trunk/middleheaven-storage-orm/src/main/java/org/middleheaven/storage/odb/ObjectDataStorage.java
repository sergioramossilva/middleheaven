//package org.middleheaven.storage.odb;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.LinkedList;
//
//import org.middleheaven.core.reflection.MethodDelegator;
//import org.middleheaven.core.reflection.ProxyHandler;
//import org.middleheaven.core.reflection.WrapperProxy;
//import org.middleheaven.core.reflection.inspection.Introspector;
//import org.middleheaven.core.reflection.inspection.ObjectInstrospector;
//import org.middleheaven.domain.query.ExecutableQuery;
//import org.middleheaven.domain.query.Query;
//import org.middleheaven.domain.query.QueryExecuter;
//import org.middleheaven.domain.store.EntityInstance;
//import org.middleheaven.domain.store.ReadStrategy;
//import org.middleheaven.domain.store.StorableState;
//import org.middleheaven.io.ManagedIOException;
//import org.middleheaven.io.repository.ManagedFile;
//import org.middleheaven.storage.StorageException;
//import org.middleheaven.storage.model.StorableFieldModel;
//import org.middleheaven.storage.storable.AbstractStorableDataStorage;
//import org.middleheaven.util.criteria.entity.CriteriaFilter;
//import org.middleheaven.util.criteria.entity.EntityCriteria;
//import org.middleheaven.util.identity.Identity;
//import org.neodatis.odb.ODB;
//import org.neodatis.odb.ODBFactory;
//import org.neodatis.odb.OID;
//import org.neodatis.odb.Objects;
//import org.neodatis.odb.core.query.IQuery;
//import org.neodatis.odb.core.query.nq.NativeQuery;
//
///**
// * TODO In construction
// */
//public class ObjectDataStorage extends AbstractStorableDataStorage {
//
//	private ManagedFile dataFile;
//	private File file;
//
//	public ObjectDataStorage(ManagedFile dataFile) {
//		super(null);
//		this.dataFile = dataFile;
//		this.file = new File(dataFile.getURI());
//	}
//
//	private ODB getDataBase(){
//		try {
//			return ODBFactory.open(file.getAbsolutePath());
//		} catch (Exception e) {
//			throw new StorageException(e);
//		}
//	}
//
//	private void closeDataBase(ODB odb){
//		try {
//			odb.close();
//		} catch (IOException e) {
//			throw new StorageException(ManagedIOException.manage(e));
//		} catch (Exception e) {
//			throw new StorageException(e);
//		}
//	}
//	
//	
//	public EntityInstance merge(Object obj){
//		EntityInstance p;
//		if (obj instanceof EntityInstance){
//			p = (EntityInstance)obj;
//		} else {
//			// not managed yet
//			ObjectInstrospector<Object> introspector = Introspector.of(obj);
//			p = introspector.newProxyInstance(new NeoDatisMethodHandler(this,obj),EntityInstance.class, WrapperProxy.class );
//
//			introspector.copyTo(p);
//		}
//		return p;
//	}
//	
//
//	private static class NeoDatisMethodHandler implements ProxyHandler {
//
//		private Object obj;
//	
//		private StorableState state = StorableState.FILLED;
//
//		private ObjectDataStorage keeper;
//
//		public NeoDatisMethodHandler(ObjectDataStorage keeper , Object obj) {
//			this.obj = obj;
//			this.keeper = keeper;
//		}
//
//		@Override
//		public Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {
//			String methodName = delegator.getName();
//			if ("getWrappedObject".equals(methodName)){
//				return obj;
//			} else if (!delegator.hasSuper()){
//				if (methodName.equals("getFieldValue")){
//					StorableFieldModel model = (StorableFieldModel)args[0];
//					String name = model.getHardName().getName();
//
//					return Introspector.of(self.getClass()).inspect().properties()
//									.named(name).retrive().getValue(self);
//					
//				} else if (methodName.equals("setFieldValue")){
//					StorableFieldModel model = (StorableFieldModel)args[0];
//					String name = model.getHardName().getName();
//
//					Introspector.of(self.getClass()).inspect().properties()
//					.named(name).retrive().setValue(self,args[1]);
//					
//					return null;
//				} else {
//					return Introspector.of(this.getClass()).inspect().methods()
//					.named(methodName).withParametersType(delegator.getInvoked().getParameterTypes()).retrive()
//					.invoke(this, args);
//				}
//			} else {
//				return delegator.invokeSuper(self, args);
//			}
//		}
//
//		public Identity getIdentity(){
//			return null; //OIDIdentity.valueOf(keeper.getDataBase().getObjectId(obj));
//		}
//		
//		public String getName() {
//			return obj.getClass().getSimpleName();
//		}
//
//		public Class<?> getPersistableClass() {
//			return obj.getClass();
//		}
//
//		public StorableState getPersistableState() {
//			return state;
//		}
//
//		public void setPersistableState(StorableState state) {
//			this.state = state;
//		}
//	}
//
//
//
//	@Override
//	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
//		return new SimpleExecutableQuery<T>(criteria,null,neoDatisQueryExecuter);
//	}
//	
//	private QueryExecuter neoDatisQueryExecuter = new QueryExecuter() {
//
//		@Override
//		public <T> Collection<T> execute(final ExecutableQuery<T> query) {
//			final EntityCriteria<T> criteria = query.getCriteria();
//			final CriteriaFilter<T> filter = new CriteriaFilter<T>(criteria,query.getModel(), ObjectDataStorage.this);
//			
//			ODB odb = getDataBase();
//			try{
//				IQuery iquery = new NativeQuery() {
//			
//				
//					public boolean match(Object obj) {
//						return filter.classify((T)obj).booleanValue();
//					}
//
//					@Override
//					public Class getObjectType() {
//						return criteria.getTargetClass();
//					}
//				};
//
//				Objects result = odb.getObjects(iquery);
//
//				Collection<T> all = new LinkedList<T>();
//
//				final int START_AT = criteria.getStart();
//				final int MAX_COUNT = criteria.getCount();
//				int elapsed=0;
//				while (elapsed<START_AT && result.hasNext()){ // will not loop if startAt==-1
//					elapsed++;
//					result.next();
//				}
//				
//				if (MAX_COUNT>=0){
//					while (result.hasNext() && all.size() < MAX_COUNT) { // add just maxCount elements
//						all.add((T)merge(result.next()));
//					}
//				} else {
//					while (result.hasNext()) {
//						all.add((T)merge(result.next()));
//					}
//				}
//				
//
//				return all;
//			}  catch (Exception e) {
//				throw new StorageException(e);
//			} finally {
//				closeDataBase(odb);
//			}
//		}
//		
//	};
//
//
//
//	@Override
//	public void insert(Collection<EntityInstance> obj) {
//
//		ODB odb = getDataBase();
//		try{
//			for (EntityInstance s : obj){
//				Object object = Introspector.of(s).unproxy();
//				OID id = odb.store(object);
//				OIDIdentity identity = OIDIdentity.valueOf(id);
//				s.setIdentity(identity);
//			}
//		}  catch (Exception e) {
//			throw new StorageException(e);
//		} finally {
//			closeDataBase(odb);
//		}
//
//	}
//
//	@Override
//	public void remove(Collection<EntityInstance> obj) {
//		ODB odb = getDataBase();
//		try{
//			for (EntityInstance s : obj){
//				Object object = Introspector.of(s).unproxy();
//				odb.delete(object);
//			}
//		}  catch (Exception e) {
//			throw new StorageException(e);
//		} finally {
//			closeDataBase(odb);
//		}
//	}
//
//	@Override
//	public void update(Collection<EntityInstance> obj) {
//		ODB odb = getDataBase();
//		try{
//			for (EntityInstance s : obj){
//				Object object = Introspector.of(s).unproxy();
//				odb.store(object);
//			}
//		}  catch (Exception e) {
//			throw new StorageException(e);
//		} finally {
//			closeDataBase(odb);
//		}
//	}
//
//
//
//	
//
//}
