package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.storage.AbstractStoreManager;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.persistent.AutoIncrementSequence;

public final class DataBaseStoreManager extends AbstractStoreManager {

	public DataBaseDialect dialect;
	public DataSource datasource;
	public AutoIncrementSequence sequence;

	public DataBaseStoreManager(DataSource datasource){
		this.datasource = datasource;
		this.dialect = DatabaseDialectFactory.getDialect(datasource);
	}

	public DataBaseStoreManager setDialect(DataBaseDialect dialect){
		this.dialect = dialect;
		return this;
	}

	public DataBaseStoreManager setDataSource(DataSource datasource){
		this.datasource = datasource;
		this.dialect = DatabaseDialectFactory.getDialect(datasource);
		return this;
	}

	@Override
	public Sequence<Long> getSequence(String name) {
		return AutoIncrementSequence.getSequence(name);
	}


	class DBStorageQuery<T> implements Query<T>{
		Criteria<T> criteria;
		StorableEntityModel model;

		public DBStorageQuery(Criteria<T> criteria, StorableEntityModel model) {
			super();
			this.criteria = criteria;
			this.model = model;
		}

		@Override
		public long count() {
			return list().size();
		}

		@Override
		public T find() {
			List<T> list = findByCriteria(criteria,model);
			return list.isEmpty() ? null : list.get(0);
		}

		@Override
		public Collection<T> list() {
			return findByCriteria(criteria,model);
		}

		@Override
		public boolean isEmpty() {
			return list().isEmpty();
		}

	} 

	<T> List<T> findByCriteria(Criteria<T> criteria,StorableEntityModel model){

		Connection con =null;
		try {
			con = this.datasource.getConnection();
			RetriveDataBaseCommand command = dialect.createSelectCommand(criteria, model);
			command.execute(con,model);
			ResultSet rs = command.getResult();
			
			try{
				// convert to List<T>
				List<T> list = new LinkedList<T>();
				ResultSetStorable s = new ResultSetStorable(rs,model);
				while (rs.next()){
					T t = merge(model.instanceFor(criteria.getTargetClass()));
					this.copy(s, (Storable)t, model);

					list.add(t);
				}
				return list;
			} finally {
				rs.close();
			}

		} catch (SQLException e){
			throw dialect.handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			}
		}
	}
	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {

		return new DBStorageQuery<T>(dialect.merge(criteria,model),model);

	}





	@Override
	public void insert(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		executeCommand(dialect.createInsertCommand(collection,model), model );

	}

	@Override
	public void remove(Collection<Storable> collection , StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		List<Long> keys = new ArrayList<Long>(collection.size());
		Storable s=null;
		for (Iterator<Storable> it = collection.iterator();it.hasNext();){
			s = it.next();
			keys.add(s.getKey());
		}
		
		Criteria<?> c = CriteriaBuilder.search(s.getPersistableClass())
		.and(model.keyFieldModel().getHardName().toString()).in(keys)
		.all();
		
		executeCommand(dialect.createDeleteCommand(c , model), model );

	}
	
	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		executeCommand(dialect.createDeleteCommand(criteria, model), model );

	}
	
	@Override
	public void update(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}

		executeCommand(dialect.createUpdateCommand(collection , model), model );

	}
	
	private void executeCommand (DataBaseCommand command,StorableEntityModel model){
		Connection con =null;
		try {
			con = this.datasource.getConnection();

			command.execute(con,model);
			

		} catch (SQLException e){
			throw dialect.handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			}
		}
	}





}
