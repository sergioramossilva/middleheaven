/**
 * 
 */
package org.middleheaven.persistance;

/**
 * 
 */
public class DataStoreSchemaName {
	
	private String database;
	private String schema;
	
	
	/**
	 * store:/provider/database/name
	 * 
	 * @param provider
	 * @param name
	 * @return
	 */
	public static DataStoreSchemaName name(String database, String name) {
		return new DataStoreSchemaName(database, name);
	}
	
	private DataStoreSchemaName(String database,
			String schema) {
		super();
		this.database = database;
		this.schema = schema;
	}
	
	public DataStoreName getDataStoreName(){
		return DataStoreName.name(database);
	}

	public String getDatabaseName() {
		return database;
	}
	public String getSchema() {
		return schema;
	}
	
	
	
}
