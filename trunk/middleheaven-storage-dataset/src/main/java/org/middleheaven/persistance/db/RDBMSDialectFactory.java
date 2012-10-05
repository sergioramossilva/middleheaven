package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.middleheaven.logging.Logger;
import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.db.dialects.HSQLDialect;
import org.middleheaven.persistance.db.dialects.MSDriverSQLServerDialect;
import org.middleheaven.persistance.db.dialects.PostgressDialect;
import org.middleheaven.persistance.db.dialects.SQLServerDialect;

/**
 * Select a data base dialect based on is own metadata
 * provided by <code>java.sql.DatabaseMetaData</code>
 * 
 *
 */
public final class RDBMSDialectFactory {

	private RDBMSDialectFactory(){}

	/**
	 * Determine the dialect that applies to the given {@link DataSource}
	 * @param dataSource the DataSource to connect to
	 * @return the correct, compatible, {@link RDBMSDialect}
	 */
	public static RDBMSDialect getDialect(DataSource dataSource){

		// finds dialect dynamically
		Connection con =null;
		try {
			con =  dataSource.getConnection();
			
			DatabaseMetaData dbm = con.getMetaData();
			return getDialectForDataBase(dbm);

		} catch (SQLException e) {
			throw new PersistanceException(e);
		} finally {
			if (con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw new PersistanceException(e);
				}
			}
		}


	}
	
	private static RDBMSDialect getDialectForDataBase (DatabaseMetaData dbm) throws SQLException{
		String product = dbm.getDatabaseProductName();
		String version = dbm.getDatabaseProductVersion();
		String driverName = dbm.getDriverName();
		String driverVersion = dbm.getDriverVersion();

		Logger.onBookFor(RDBMSDialectFactory.class).info(
				"Inicializing dialect for: {0} {1} usign driver {2} {3}" ,  
				product, 
				version,
				driverName, 
				driverVersion
		);

		if (product.equalsIgnoreCase("Microsoft SQL Server")){
			if (driverName.toLowerCase().startsWith("sqlserver")){
				return new MSDriverSQLServerDialect(); // supports version 08.00.0760 with Microsoft Driver 2.2.0022
			} else {
				return new SQLServerDialect(); // supports version 08.00.0760 with jTDS
			}
		} else if (product.equalsIgnoreCase("PostgreSQL")){
			return new PostgressDialect(); // supports 8.1.3 with corresponding driver and 9.0 with corresponding driver
		} else if (product.toUpperCase().startsWith("HSQL")){
			return new HSQLDialect(); // supports 1.8.0 with corresponding driver
		} else {
			throw new RDBMSException("Dialect not found for product " + product + "  " + version);
		}

	}

}
