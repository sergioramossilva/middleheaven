package org.middleheaven.persistance.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.model.ColumnType;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;

public class PreparedStatementStorable {

	private final PreparedStatement ps;
	private final DataBaseMapper mapper;

	public PreparedStatementStorable( DataBaseMapper mapper, PreparedStatement ps) {
		this.ps = ps; 
		this.mapper = mapper;

	}
	
	public void copy(DataRow row) throws SQLException {

		int index = 1;
		
		for ( DataColumn cm : row){

			setColumn(index, cm.getValue() , mapper.getColumnModel(cm.getModel().getName()));
			index++;
		}
	}

	private int infereSQLType(ColumnType dataType){
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


	public void setColumn(int i, Object value, DBColumnModel cm ) throws SQLException {

		if (value == null){
			ps.setNull(i, infereSQLType(cm.getType())); 
		} else if (value instanceof LongIdentity){
			ps.setLong(i,((LongIdentity)value).longValue());
		} else if (value instanceof IntegerIdentity){
			ps.setInt(i, ((IntegerIdentity)value).intValue());
		}  else if (cm.getType().isTemporal()){ 
			if (value instanceof Date ) {
				ps.setTimestamp(i, new Timestamp(((Date)value).getTime()));
			} else if (value instanceof Calendar ) {
				ps.setTimestamp(i, new Timestamp(((Calendar)value).getTimeInMillis()));
			} else if (value instanceof CalendarDate){
				ps.setTimestamp(i, new Timestamp(((CalendarDate)value).getMilliseconds()));
			} else if (value instanceof CalendarDateTime){
				ps.setTimestamp(i, new Timestamp(((CalendarDateTime)value).getMilliseconds()));
			}
		}  else {
			switch (cm.getType()){
			case TEXT:
				ps.setString(i, value.toString());
				break;
			case INTEGER:
				if (value instanceof Long){
					ps.setLong(i,((Long)value).longValue());
				} else if (value instanceof Integer){
					ps.setInt(i, ((Integer)value).intValue());
				} 
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
