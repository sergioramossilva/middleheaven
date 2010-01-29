package org.middleheaven.domain;

import java.math.BigDecimal;
import java.util.Date;

public enum DataType {
	
	UNKWON,
	LOGIC,
	ENUM,
	STATUS,
	TEXT,
	MEMO,
	INTEGER,
	DECIMAL,
	DATETIME,
	TIME,
	DATE,
	ONE_TO_ONE,
	ONE_TO_MANY,
	MANY_TO_ONE, 
	MANY_TO_MANY;
	
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
		return this.equals(ONE_TO_ONE) || this.equals(MANY_TO_ONE);
	}

	
	public boolean isToManyReference() {
		return this.equals(ONE_TO_MANY) || this.equals(MANY_TO_MANY);
	}

	public boolean isTemporal(){
		return  this.equals(DATETIME) || this.equals(TIME) || this.equals(DATE);
	}
	
	public boolean isVirtual() {
		return this.equals(ONE_TO_ONE) || this.equals(MANY_TO_ONE) || this.equals(ONE_TO_MANY);
	}

	public boolean isReference() {
		return isToOneReference() || isToManyReference() ;
	}

	public boolean isDecimal() {
		return this.equals(DECIMAL);
	}

	public boolean isTextual() {
		return this.equals(TEXT);
	}
}
