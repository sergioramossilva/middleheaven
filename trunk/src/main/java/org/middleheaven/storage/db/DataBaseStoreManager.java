package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.storage.EmptyQuery;
import org.middleheaven.storage.ListQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StoreManager;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.persistent.AutoIncrementSequence;

public final class DataBaseStoreManager implements StoreManager {

	public DataBaseDialect dialect;
	public DataSource datasource;
	public AutoIncrementSequence sequence;

	public DataBaseStoreManager(){

	}

	public void setDialect(DataBaseDialect dialect){
		this.dialect = dialect;
	}

	public void setDataSource(DataSource datasource){
		this.datasource = datasource;
	}

	@Override
	public Sequence<Long> getSequence(String name) {
		return AutoIncrementSequence.getSequence(name);
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {

		Connection con =null;
		try {
			con = this.datasource.getConnection();
			DataBaseCommand command = dialect.createSelectCommand(criteria, model);
			PreparedStatement ps = command.getStatement(con);

			ResultSet rs = ps.executeQuery();
			if (rs.isLast()){
				try{
					return new EmptyQuery<T>();
				} finally {
					rs.close();
				}
			} else if (strategy.isFowardOnly() && strategy.isReadOnly()){
				rs.beforeFirst();
				return new FastlaneQuery<T>(rs);
			} else {
				try{
					rs.beforeFirst();
					// convert to List<T>
					List<T> list = new LinkedList<T>();
					ResultSetStorable s = new ResultSetStorable(rs);
					while (rs.next()){
						list.add(model.instanceFor(criteria.getTargetClass(), s));
					}
					return new ListQuery<T>(list);
				} finally {
					rs.close();
				}
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
	public void insert(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		executeCommand(collection , dialect.createInsertCommand(model) );
		
	}

	@Override
	public void remove(Collection<Storable> collection , StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		executeCommand(collection , dialect.createDeleteCommand(model) );
		
	}

	@Override
	public void update(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}

		executeCommand(collection , dialect.createUpdateCommand(model) );
		
	}
	
	private void executeCommand (Collection<Storable> collection, DataBaseCommand command){
		Connection con =null;
		try {
			con = this.datasource.getConnection();
			
			PreparedStatement ps = command.getStatement(con);
			PreparedStatementStorable pss = new PreparedStatementStorable(ps);
			for (Storable s : collection){
				pss.copy(s);
				ps.addBatch();
			}
			ps.executeBatch();
			
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
