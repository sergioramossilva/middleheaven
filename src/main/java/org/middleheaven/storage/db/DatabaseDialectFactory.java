package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.middleheaven.logging.Logging;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.dialects.HSQLDialect;
import org.middleheaven.storage.db.dialects.MSDriverSQLServerDialect;
import org.middleheaven.storage.db.dialects.PostgressDialect;
import org.middleheaven.storage.db.dialects.SQLServerDialect;

/**
 * Select a data base dialect based on is own metadata
 * provided by <code>java.sql.DatabaseMetaData</code>
 * 
 *
 */
public final class DatabaseDialectFactory {

	private DatabaseDialectFactory(){}

	public static DataBaseDialect getDialect(DataSource dataSource){

		// finds dialect dynamically
		Connection con =null;
		try {
			con =  dataSource.getConnection();
			DatabaseMetaData dbm = con.getMetaData();
			return getDialectForDataBase(dbm);

		} catch (SQLException e) {
			throw new StorageException(e);
		} finally {
			if (con!=null)
				try {
					con.close();
				} catch (SQLException e) {
					throw new StorageException(e);
				}
		}


	}
	private static DataBaseDialect getDialectForDataBase (DatabaseMetaData dbm) throws SQLException{
		String product = dbm.getDatabaseProductName();
		String version = dbm.getDatabaseProductVersion();
		String driverName = dbm.getDriverName();
		String driverVersion = dbm.getDriverVersion();

		Logging.getBook(DatabaseDialectFactory.class).logInfo("Inicializing dialect for: " + product + "  " + version + " usign driver " + driverName + " " + driverVersion );

		if (product.equalsIgnoreCase("Microsoft SQL Server")){
			if (driverName.toLowerCase().startsWith("sqlserver")){
				return new MSDriverSQLServerDialect(); //version 08.00.0760 Microsoft Driver 2.2.0022
			} else {
				return new SQLServerDialect(); // version 08.00.0760 jTDS
			}
		} else if (product.equalsIgnoreCase("PostgreSQL")){
			return new PostgressDialect(); // 8.1.3 any driver
		} else if (product.toUpperCase().startsWith("HSQL")){
			return new HSQLDialect(); // 1.8.0 any driver
		} else {
			throw new StorageException("Dialect not found for product " + product + "  " + version);
		}

	}

}
