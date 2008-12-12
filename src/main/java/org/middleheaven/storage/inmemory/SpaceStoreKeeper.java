package org.middleheaven.storage.inmemory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.storage.AbstractStoreKeeper;
import org.middleheaven.storage.PersistableState;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.sequence.CannotCreateSequenceException;
import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.SequenceException;
import org.middleheaven.util.sequence.SequenceToken;
import org.space4j.CommandException;
import org.space4j.LoggerException;
import org.space4j.Space;
import org.space4j.Space4J;
import org.space4j.command.CreateMapCmd;
import org.space4j.command.CreateSequenceCmd;
import org.space4j.command.IncrementSeqCmd;
import org.space4j.command.PutCmd;
import org.space4j.command.RemoveCmd;
import org.space4j.indexing.IndexManager;

public class SpaceStoreKeeper extends AbstractStoreKeeper {

	protected Space4J space4j = null;
	protected Space space;
	protected IndexManager im;

	final Map<String, Sequence<Long>> sequences = new HashMap<String,Sequence<Long>>();

	public SpaceStoreKeeper(Space4J space4j){

		this.space4j = space4j;

		space = space4j.getSpace();

		im = space.getIndexManager();

	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, StorableEntityModel model, ReadStrategy strategy) {
		return new SpaceQuery<T>(criteria, model);
	}

	@Override
	public Sequence<Identity> getSequence(String name) {
		/*
		 * Create the sequence for the unique ids
		 * using the Space4J CreateSequenceCmd.
		 */
		if (!space4j.getSpace().check(name)) {

			try {
				space4j.exec(new CreateSequenceCmd(name));
			} catch (CommandException e) {
				throw new CannotCreateSequenceException(e);
			} catch (LoggerException e) {
				throw new CannotCreateSequenceException(e);
			}
		} 

		return new SpaceSequence(name);

	}

	@Override
	public void insert(Collection<Storable> list, StorableEntityModel model) {

		// Grab the space where all data resides...

		final String entityRef = model.getEntityHardName(); 

		try {


			/*
			 * If this is the first time, create our map that will hold our
			 * User objects. Space4J comes with many ready-to-use commands.
			 * One of them is the CreateMapCmd, that will create our initial
			 * empty map that to hold our objects.
			 */
			if (!space.check(entityRef)) {


				space4j.exec(new CreateMapCmd(entityRef));

			}

			for (Storable s : list){
				space4j.exec(new PutCmd(entityRef, s.getIdentity(), serialize(s,model)));

			}

		} catch (CommandException e) {
			throw new StorageException(e);
		} catch (LoggerException e) {
			throw new StorageException(e);
		}
	}

	public Storable serialize(Storable other,StorableEntityModel model){
		SpaceStorable ss = new SpaceStorable();

		ss.key = other.getIdentity();
		ss.state = other.getPersistableState();
		ss.persistableClassName = other.getPersistableClass().getName();
		for (StorableFieldModel fm : model.fields()){
			ss.setFieldValue(fm, other.getFieldValue(fm));
		}
		return ss;
	}


	@Override
	public void remove(Collection<Storable> obj, StorableEntityModel model) {
		final String entityRef = model.getEntityHardName(); 

		try{
			for (Storable s : obj){
				space4j.exec(new RemoveCmd(entityRef, s.getIdentity()));
			}

		} catch (CommandException e) {
			throw new StorageException(e);
		} catch (LoggerException e) {
			throw new StorageException(e);
		}

	}

	@Override
	public void update(Collection<Storable> obj, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}

	private static class SpaceStorable implements Storable, Serializable{

		Identity key;
		String persistableClassName;
		PersistableState state;
		Map<String, Object> values = new TreeMap<String, Object>();

		public SpaceStorable(){

		}

		@Override
		public Object getFieldValue(StorableFieldModel model) {
			return values.get(model.getHardName().toString());
		}

		public void setFieldValue(StorableFieldModel model,Object value) {
			values.put(model.getHardName().toString(),value);
		}

		@Override
		public Identity getIdentity() {
			return key;
		}

		@Override
		public void setIdentity(Identity id) {
			this.key = id;
		}

		@Override
		public Class<?> getPersistableClass() {
			return ReflectionUtils.loadClass(persistableClassName);
		}

		@Override
		public PersistableState getPersistableState() {
			return state;
		}

		@Override
		public void setPersistableState(PersistableState state) {
			this.state = state;
		}





	}

	private class SpaceSequence implements Sequence<Identity> {

		public String seqName;

		public SpaceSequence(String seqName){
			this.seqName=seqName;
		}

		@Override
		public SequenceToken<Identity> next() {
			/*
			 * Increment the sequence and return it,
			 * by using the IncrementSeqCmd command.
			 */
			try {
				return new DefaultToken<Identity>(new IntegerIdentity(new Long(space4j.exec(new IncrementSeqCmd(seqName))).intValue()));
			} catch (CommandException e) {
				throw new SequenceException(e);
			} catch (LoggerException e) {
				throw new SequenceException(e);
			}
		}

	}

	private class SpaceQuery<T> implements Query<T>{

		Criteria<T> criteria;
		StorableEntityModel model;
		public SpaceQuery(Criteria<T> criteria,StorableEntityModel model){
			this.criteria = criteria;
			this.model = model;
		}

		@Override
		public long count() {
			return list().size();
		}

		@Override
		public T find() {
			return list().iterator().next();
		}

		@Override
		public Collection<T> list() {

			Collection<T> results = new LinkedList<T>();

			BooleanClassifier<T> filter = new CriteriaFilter<T> (criteria);

			Iterator<Object> iter = space.getIterator(model.getEntityHardName());

			if (iter!=null){
				while (iter.hasNext()) {
					T elem = (T)iter.next();
					if (filter.classify(elem)){
						results.add(elem);
					}
				}
			}

			return results;
		}

		@Override
		public boolean isEmpty() {
			return list().isEmpty();
		}

	}

	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}
}
