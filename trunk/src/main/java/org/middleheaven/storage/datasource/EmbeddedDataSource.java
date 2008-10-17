package org.middleheaven.storage.datasource;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.middleheaven.logging.Logging;


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
	
	public EmbeddedDataSource(URL location, String catalog, String login, String pass) throws ClassNotFoundException{
		
		this.location = location;
		this.catalog = catalog;
		this.url = "jdbc:hsqldb:hsql://localhost/" + catalog;
		this.login = login;
		this.pass = pass;
		
		try {
			Class.forName("org.hsqldb.jdbcDriver" ).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	  

	}
	
	public void start(){
		
		Logging.getBook(this.getClass()).logInfo("Starting :" + url);
		
		String[] params = new String[4];
		params[0] = "-database.0";
		params[1] = location.toString();
		params[2] = "-dbname.0";
		params[3] = catalog;

		org.hsqldb.Server.main(params);
	}
	
	public void stop() throws SQLException{
		
		Logging.getBook(this.getClass()).logInfo("Stopping :" + url);
		
		Connection con=null;
		try {
			con = this.getConnection();
			
			con.setAutoCommit(true);
			con.prepareStatement("SHUTDOWN").execute();
			
		} finally {
			if (con!=null){
				con.close();
			}
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, login, pass);
	}

	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		return DriverManager.getConnection(url, nlogin,npass);
	}

}
