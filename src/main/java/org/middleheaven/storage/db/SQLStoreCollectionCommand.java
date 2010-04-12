package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.assembly.AssemblyContext;
import org.middleheaven.storage.assembly.AssemblyLineService;
import org.middleheaven.storage.assembly.Data;
import org.middleheaven.storage.assembly.SimpleAssemblyLine;

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
	public boolean execute(DataBaseStorage storage, Connection connection) throws SQLException {

		if (data.size() > 1 && dialect.supportsBatch()){
			AssemblyLineService lines = new SimpleAssemblyLine();
			
			PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			PreparedStatementStorable pss = new PreparedStatementStorable(storage,ps);
			for (Storable s : data){
				
				AssemblyContext context = AssemblyContext.contextualize(s);
				
				
				lines.unAssemble(context);
				
				context.iterator();
				
				pss.copy(s, fields);
				ps.addBatch();
				
			}
			return ps.executeBatch().length>0;
		} else if (!data.isEmpty()){
			PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			int count = 0;
			for (Storable s : data){
				PreparedStatementStorable pss = new PreparedStatementStorable(storage,ps);
				pss.copy(s, fields);
			
				count += ps.executeUpdate();
			}
			
			return count >0;
		} else {
			return false;
		}
		

	}

	@Override
	public String getSQL() {
		return sql;
	}


}
