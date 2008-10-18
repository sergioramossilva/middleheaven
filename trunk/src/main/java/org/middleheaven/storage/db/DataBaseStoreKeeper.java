package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.middleheaven.storage.AbstractStoreKeeper;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;
import org.middleheaven.util.identity.IntegerIdentitySequence;
import org.middleheaven.util.sequence.Sequence;

public final class DataBaseStoreKeeper extends AbstractStoreKeeper {

	private DataBaseDialect dialect;
	private DataSource datasource;

	public DataBaseStoreKeeper(DataSource datasource){
		this.datasource = datasource;
		this.dialect = DatabaseDialectFactory.getDialect(datasource);
	}

	public DataBaseStoreKeeper setDialect(DataBaseDialect dialect){
		this.dialect = dialect;
		return this;
	}

	public DataBaseStoreKeeper setDataSource(DataSource datasource){
		this.datasource = datasource;
		this.dialect = DatabaseDialectFactory.getDialect(datasource);
		return this;
	}

	
	Map<String, IdentitySequence> sequences = new TreeMap<String,IdentitySequence>();
	
	@Override
	public <I extends Identity> Sequence<I> getSequence(String name) {
		
		IdentitySequence iseq = sequences.get(name);
		if (iseq==null){
			Sequence<Long> seq = dialect.getSequence(datasource, name);
			iseq = new IntegerIdentitySequence();
			
		}
		return iseq;
		
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
					T t = merge(criteria.getTargetClass().cast(model.newInstance()));
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
		List<Identity> keys = new ArrayList<Identity>(collection.size());
		Storable s=null;
		for (Iterator<Storable> it = collection.iterator();it.hasNext();){
			s = it.next();
			keys.add(s.getIdentity());
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
