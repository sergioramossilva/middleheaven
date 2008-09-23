package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;

public class SQLStoreCollectionCommand implements DataBaseCommand {

	final private String sql;
	final private Collection<StorableFieldModel> fields;
	final private Collection<Storable> data;
	
	protected SQLStoreCollectionCommand(Collection<Storable> data, String sql,Collection<StorableFieldModel> fields){
		this.data = data;
		this.sql = sql;
		this.fields = fields;
	}
	
	protected SQLStoreCollectionCommand(Collection<Storable> data , String sql){
		this.data = data;
		this.sql = sql;
		this.fields = Collections.emptyList();
	}
	
	@Override
	public void execute(Connection con,StorableEntityModel model) throws SQLException {
		
		PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
		
		PreparedStatementStorable pss = new PreparedStatementStorable(ps);
		for (Storable s : data){
			pss.copy(s, fields);
			ps.addBatch();
		}
		ps.executeBatch();

	}


}
