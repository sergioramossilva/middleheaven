package org.middleheaven.data;

import java.math.BigDecimal;
import java.util.Date;

public enum DataType {
	
	UNKWON,
	LOGIC,
	TEXT,
	INTEGER,
	DECIMAL,
	DATETIME,
	TIME,
	DATE,
	ONE2ONE,
	ONE2MANY,
	MANY2ONE;
	
	public static DataType fromClass(Class<?> type){
		if (Date.class.isAssignableFrom(type)){
			return DataType.DATETIME;
		}else if (Long.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)){
			return DataType.INTEGER;
		}else if (Double.class.isAssignableFrom(type) || BigDecimal.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)){
			return DataType.INTEGER;
		}else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)){
			return DataType.LOGIC;
		}else if (CharSequence.class.isAssignableFrom(type)){
			return DataType.TEXT;
		} else {
			throw new IllegalArgumentException("Unkown datatype " + type.getName() );
		}
	}
	
	public boolean isToOneReference() {
		return this.equals(ONE2ONE) || this.equals(MANY2ONE);
	}

	
	public boolean isToManyReference() {
		return this.equals(ONE2MANY);
	}

	public boolean isTemporal(){
		return  this.equals(DATETIME) || this.equals(TIME) || this.equals(DATE);
	}
	
	public boolean isVirtual() {
		return this.equals(ONE2ONE) || this.equals(MANY2ONE) || this.equals(ONE2MANY);
	}
}
