package org.middleheaven.storage.datasource;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.logging.Logging;
import org.middleheaven.storage.StorageException;


/**
 * Utilizes a HSQL database that is started by this in server mode
 * or in-process  mode.
 */
public class EmbeddedDataSource extends AbstractDataSource {

	private String catalog;
	private String login;
	private String pass;
	private String url;
	private URL location;
	
	public EmbeddedDataSource(URL location, String catalog, String login, String pass) {
		
		this.location = location;
		this.catalog = catalog;
		this.url = "jdbc:hsqldb:hsql://localhost/" + catalog;
		this.login = login;
		this.pass = pass;
		
		try {
			Class.forName("org.hsqldb.jdbcDriver" ).newInstance();
		} catch (ClassNotFoundException e) {
            throw new DriverNotFoundException("Driver org.hsqldb.jdbcDriver was not found.");
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	  

	}
	
	public void start(){
		
		Logging.getBook(this.getClass()).info("Starting :" + url);
		
		String[] params = new String[4];
		params[0] = "-database.0";
		params[1] = location.toString();
		params[2] = "-dbname.0";
		params[3] = catalog;

		Introspector.of(Object.class).load("org.hsqldb.Server").invokeMain(params);

	}
	
	public void stop(){
		
		Logging.getBook(this.getClass()).info("Stopping :" + url);
		
		Connection con=null;
		try{
		try {
			con = this.getConnection();
			
			con.setAutoCommit(true);
			con.prepareStatement("SHUTDOWN").execute();
			
		} finally {
			if (con!=null){
				con.close();
			}
		}
		} catch (SQLException e){
			throw new StorageException(e);
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(url, login, pass);
		con.setAutoCommit(isAutoCommit());
		return con;
	}

	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		Connection con = DriverManager.getConnection(url, nlogin,npass);
		con.setAutoCommit(isAutoCommit());
		return con;
	}

}
