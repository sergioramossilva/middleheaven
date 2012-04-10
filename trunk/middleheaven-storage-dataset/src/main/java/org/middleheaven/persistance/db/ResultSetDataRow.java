package org.middleheaven.persistance.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import org.middleheaven.persistance.DataAccessException;
import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataColumnsModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.TransformedIterator;

/**
 * 
 */
class ResultSetDataRow implements DataRow{
	

	public static ResultSetDataRow newInstance (ResultSet resultSet, RDBMSDialect dialect) throws SQLException{
		return new ResultSetDataRow(resultSet, resultSet.getMetaData() , dialect);
	}
	
	private ResultSet resultSet;
	private DataColumnsModel model;

	
	private ResultSetDataRow (ResultSet resultSet, ResultSetMetaData resultSetMetaData, RDBMSDialect dialect){
		this.resultSet = resultSet;
		this.model = new ResultSetMetaDataDataColumnsModel(resultSetMetaData, dialect);
	}

	@Override
	public Iterator<DataColumn> iterator() {
		return TransformedIterator.transform(model.iterator(), new Classifier<DataColumn, DataColumnModel>(){

			@Override
			public DataColumn classify(DataColumnModel next) {
				return getColumn(next.getName());
			}
			
		});
	}

	@Override
	public DataColumn getColumn(final QualifiedName name) {
		return new DataColumn(){

			@Override
			public DataColumnModel getModel() {
				return model.getDataColumnModel(name.getName());
			}

			@Override
			public Object getValue() {
				try {
					return resultSet.getObject(name.getName());
				} catch (SQLException e) {
					throw new DataAccessException(e);
				}
			}

			@Override
			public void setValue(Object value) {
				throw new UnsupportedOperationException("This DataColumn is read only");
			}
			
			
		};
	}
	
}