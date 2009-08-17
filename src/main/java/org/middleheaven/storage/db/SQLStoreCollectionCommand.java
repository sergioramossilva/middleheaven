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
	final private DataBaseDialect dialect;

	
	public SQLStoreCollectionCommand(DataBaseDialect dialect , Collection<Storable> data, String sql,Collection<StorableFieldModel> fields){
		this.dialect = dialect;
		this.data = data;
		this.sql = sql;
		this.fields = fields;
	}
	
	protected SQLStoreCollectionCommand(DataBaseDialect dialect ,Collection<Storable> data , String sql){
		this.dialect = dialect;
		this.data = data;
		this.sql = sql;
		this.fields = Collections.emptyList();
	}
	
	public DataBaseDialect getDialect(){
		return dialect;
	}
	
	@Override
	public boolean execute(DataBaseStorage keeper, Connection con, StorableEntityModel model) throws SQLException {
		
		if (data.size() > 1 && dialect.supportsBatch()){
			PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			PreparedStatementStorable pss = new PreparedStatementStorable(keeper,ps);
			for (Storable s : data){
				pss.copy(s, fields);
				ps.addBatch();
			}
			return ps.executeBatch().length>0;
		} else if (!data.isEmpty()){
			PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			int count = 0;
			for (Storable s : data){
				PreparedStatementStorable pss = new PreparedStatementStorable(keeper,ps);
				pss.copy(s, fields);
			
				count += ps.executeUpdate();
			}
			
			return count >0;
		} else {
			return false;
		}
		

	}


}
