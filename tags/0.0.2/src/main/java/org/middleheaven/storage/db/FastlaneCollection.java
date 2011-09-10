package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.StorableStateManager;
import org.middleheaven.storage.StorageException;

public class FastlaneCollection<T> implements Collection<T> {

	ResultSet rs;
	Connection connection;
	DataBaseStorage dataStorage;
	StorableEntityModel model;
	long maxCount;
	long count=0;
	private StorableStateManager manager;

	public FastlaneCollection(long maxCount,ResultSet rs, Connection connection,StorableEntityModel model,
			DataBaseStorage dataStorage, StorableStateManager manager) {
		this.rs = rs;
		this.connection = connection;
		this.dataStorage = dataStorage;
		this.model = model;
		this.maxCount= maxCount;
		this.manager = manager;

	}

	@Override
	public boolean isEmpty() {
		return maxCount==0;
	}

	@Override
	public int size() {
		return (int)maxCount;
	}

	private void release(){
		try{
			rs.close();
			connection.close();
		} catch (SQLException e){
			throw new StorageException(e);
		}
	}

	private <E> E newInstance(Class<E> type){
		return Introspector.of(type).newInstance();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator <T>(){

			@Override
			public boolean hasNext() {
				boolean hasNext = count < maxCount;

				if (!hasNext){
					release();
				}

				return hasNext;
			}

			@Override
			public T next() {
				count++;
				try {
					if ( rs.next() ){
						final ResultSetStorable s = new ResultSetStorable(rs,model);
						Storable t = manager.merge(newInstance(model.getEntityClass()));
						t = dataStorage.copyStorable(s, t, model);
						t.setStorableState(StorableState.RETRIVED);
						return (T)t;
					} else {
						release();
						throw new IndexOutOfBoundsException("No more records");
					}
				} catch (SQLException e){
					throw new StorageException(e);
				}

			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}


	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}



}
