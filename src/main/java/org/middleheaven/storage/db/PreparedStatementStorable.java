package org.middleheaven.storage.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.middleheaven.domain.DataType;
import org.middleheaven.domain.DataTypeModel;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;
import org.middleheaven.util.identity.StringIdentity;
import org.middleheaven.util.identity.UUIDIdentity;

public class PreparedStatementStorable {

	PreparedStatement ps;
	DataBaseStorage keeper;
	
	public PreparedStatementStorable(DataBaseStorage keeper, PreparedStatement ps) {
		this.ps = ps; 
		this.keeper = keeper;

	}
	public void copy(Storable s,Iterable<StorableFieldModel> fields) throws SQLException {

		int index = 1;
		for ( StorableFieldModel fm : fields){
			setField(index,s.getFieldValue(fm) , fm.isIdentity(), fm.getDataTypeModel());
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

	private DataType resolveDataType(Object value){
		DataType dt = DataType.fromClass(value.getClass());
//		if (dt.equals(DataType.UNKWON)){
//			if (value instanceof LongIdentity || value instanceof IntegerIdentity){
//				return DataType.INTEGER;
//			} else if (value instanceof StringIdentity || value instanceof UUIDIdentity){
//				return DataType.TEXT;
//			} else {
//				throw new UnsupportedOperationException(value.getClass() + " cannot be preseved");
//			}
//		}
		return dt;
	}
	
	public void setField(int i, Object value, StorableFieldModel fm ) throws SQLException {
		setField(i,value,fm.isIdentity(), fm.getDataTypeModel());
	}
	
	private void setField(int i, Object value, boolean isIdentity , DataTypeModel dataTypeModel ) throws SQLException {
		
		if (value == null){
			ps.setNull(i, infereSQLType(dataTypeModel.getDataType())); 
		} else if (dataTypeModel.getDataType().isToOneReference()){
			final Identity id = keeper.getIdentityFor(value);
			setField(i, id , false, dataTypeModel);
			
		} else if (dataTypeModel.getDataType().isToManyReference()){
			return;
		} else if (dataTypeModel.getDataType().isTemporal()){ 
			if (value instanceof Date ) {
				ps.setTimestamp(i, new Timestamp(((Date)value).getTime()));
			} else if (value instanceof Calendar ) {
				ps.setTimestamp(i, new Timestamp(((Calendar)value).getTimeInMillis()));
			} else if (value instanceof CalendarDate){
				ps.setTimestamp(i, new Timestamp(((CalendarDate)value).getMilliseconds()));
			} else if (value instanceof CalendarDateTime){
				ps.setTimestamp(i, new Timestamp(((CalendarDateTime)value).getMilliseconds()));
			}
		} else if (isIdentity){
			//setField(i, value, false, this.resolveDataType(value));
			setField(i, value, false, dataTypeModel);
		} else {
			switch (dataTypeModel.getDataType()){
			case TEXT:
				ps.setString(i, value.toString());
				break;
			case INTEGER:
				if (value instanceof Long){
					ps.setLong(i,((Long)value).longValue());
				} else if (value instanceof Integer){
					ps.setInt(i, ((Integer)value).intValue());
				} else if (value instanceof LongIdentity){
					ps.setLong(i,((LongIdentity)value).longValue());
				} else if (value instanceof IntegerIdentity){
					ps.setInt(i, ((IntegerIdentity)value).intValue());
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
