package org.middleheaven.persistance.model;

/**
 * The type of columns that can be used.
 */
public enum ColumnValueType {

	/**A textual value.*/
	TEXT,
	/**  A limitless textual value **/
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
	 * {@inheritDoc}
	 */
	public boolean isTemporal(){
		return this == DATE || this == TIME || this == DATETIME;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTextual() {
		return this == TEXT || this == CLOB || this == MEMO;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDecimal() {
		return this == DECIMAL;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInteger() {
		return this == INTEGER || this == SMALL_INTEGER;
	}
}
