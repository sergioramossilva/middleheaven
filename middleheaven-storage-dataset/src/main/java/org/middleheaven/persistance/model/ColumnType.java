package org.middleheaven.persistance.model;

/**
 * The type of columns that can be used.
 */
public enum ColumnType {

	/**A textual value.*/
	TEXT,
	/**A large (limitless) textual value.*/
	MEMO,
	/**A large binary object value.*/
	BLOB,
	/**A large character object value.*/
	CLOB,
	/**An integer value*/
	INTEGER,
	/**An small integer value (less than 256)*/
	SMALL_INTEGER,
	/**A decimal value*/
	DECIMAL,
	/**A date only value (does not include time)*/
	DATE,
	/**A time only value (does not include date).*/
	TIME,
	/**A date and time value.*/
	DATETIME,
	/**A boolean value with values TRUE and FALSE.*/
	LOGIC;
	
	/**
	 * 
	 * @return <code>true</code> if this a temporal value, <code>false</code> otherwise;
	 */
	public boolean isTemporal(){
		return this == DATE || this == TIME || this == DATETIME;
	}

	/**
	 * 
	 * @return <code>true</code> if this a textual value, <code>false</code> otherwise;
	 */
	public boolean isTextual() {
		return this == TEXT || this == MEMO;
	}

	/**
	 * 
	 * @return <code>true</code> if this a decimal value, <code>false</code> otherwise;
	 */
	public boolean isDecimal() {
		return this == DECIMAL;
	}

	/**
	 * @return
	 */
	public boolean isInteger() {
		return this == INTEGER || this == SMALL_INTEGER;
	}
}