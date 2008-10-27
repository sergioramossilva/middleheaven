package org.middleheaven.storage.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.middleheaven.data.DataType;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;

public class PreparedStatementStorable {

	PreparedStatement ps;
	public PreparedStatementStorable(PreparedStatement ps) {
		this.ps = ps; 

	}
	public void copy(Storable s,Iterable<StorableFieldModel> fields) throws SQLException {

		int index = 1;
		for ( StorableFieldModel fm : fields){
			setField(index,s.getFieldValue(fm) , fm.getDataType());
			index++;
		}
	}

	private int infereSQLType(DataType dataType){
		switch (dataType){
		case INTEGER:
			return Types.BIGINT;
		case DATETIME:
			return Types.TIMESTAMP;
		case TIME:
			return Types.TIME;
		case DATE:
			return Types.DATE;
		case LOGIC:
			return Types.BIT;
		case TEXT:
		default:
			return Types.VARCHAR;
		}

	}

	public void setField(int i, Object value, DataType dataType ) throws SQLException {


		if (value == null){
			ps.setNull(i, infereSQLType(dataType)); 
		} else if (dataType.isTemporal()){ 
			if (value instanceof Date ) {
				ps.setTimestamp(i, new Timestamp(((Date)value).getTime()));
			} else if (value instanceof Calendar ) {
				ps.setTimestamp(i, new Timestamp(((Calendar)value).getTimeInMillis()));
			}
		} else {
			switch (dataType){
			case TEXT:
				ps.setString(i, value.toString());
				break;
			case INTEGER:
			case IDENTITY:
				ps.setLong(i, Long.parseLong(value.toString()));
				break;
			case LOGIC:
				ps.setBoolean(i, Boolean.parseBoolean(value.toString()));
				break;
			default:
				throw new UnsupportedOperationException(value.getClass() + " cannot be set");
			}

		}

	}


}
