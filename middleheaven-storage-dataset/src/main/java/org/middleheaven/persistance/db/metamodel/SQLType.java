package org.middleheaven.persistance.db.metamodel;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum version of {@link java.sql.Types}.
 */
public enum SQLType {

	
	ARRAY (Types.ARRAY),
	BIGINT(Types.BIGINT), 
    BINARY(Types.BINARY), 
    BIT(Types.BIT), 
    BLOB(Types.BLOB), 
    BOOLEAN(Types.BOOLEAN), 
    CHAR(Types.CHAR), 
    CLOB (Types.CLOB),
    DATALINK(Types.DATALINK), 
    DATE (Types.DATE),
    DECIMAL (Types.DECIMAL),
    DISTINCT (Types.DISTINCT),
    DOUBLE (Types.DOUBLE),
    FLOAT (Types.FLOAT),
    INTEGER (Types.INTEGER),
    JAVA_OBJECT (Types.JAVA_OBJECT),
    LONGNVARCHAR (Types.LONGNVARCHAR),
    LONGVARBINARY (Types.LONGVARBINARY),
    LONGVARCHAR (Types.LONGVARCHAR),
    NCHAR (Types.NCHAR),
    NCLOB (Types.NCLOB),
    NUMERIC (Types.NUMERIC),
    NVARCHAR (Types.NVARCHAR),
    OTHER (Types.OTHER),
    ROWID(Types.ROWID),
    REAL (Types.REAL),
    REF (Types.REF),
    SMALLINT (Types.SMALLINT),
    STRUCT (Types.STRUCT),
    TIME (Types.TIME),
    TIMESTAMP (Types.TIMESTAMP),
    TINYINT (Types.TINYINT),
    VARBINARY (Types.VARBINARY),
    VARCHAR (Types.VARCHAR);
	
	private final static Map<Integer, SQLType> types = new HashMap<Integer, SQLType>(); 
	
	static {
		for (SQLType t : SQLType.values()) {
			types.put(t.getJDBCEQuivalent(), t);
		}
	}
	
	/**
	 * Resolves the SQLTypes objet that is equivalent to the int jdbc sql.Types value
	 * @param value the jdbc equivalent.
	 * @return the SQLTypes object.
	 */
	public static SQLType valueFromJDBCEquivalent(int value){
		return types.get(Integer.valueOf(value));
	}
	
	private int sqlType;

	private SQLType (int sqlType){
		this.sqlType = sqlType;
	}
	
	public int getJDBCEQuivalent(){
		return sqlType;
	}


}
