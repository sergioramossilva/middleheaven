package org.middleheaven.storage.db.datasource;

import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Logging;
import org.middleheaven.storage.StorageException;
import org.middleheaven.transactions.Transaction;
import org.middleheaven.transactions.TransactionService;
import org.middleheaven.transactions.XAResourceAdapter;


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
	private boolean inMemory;

	public EmbeddedDataSource(URL location, String catalog, String login, String pass) {
		this(false,location,catalog,login,pass);
	}

	public EmbeddedDataSource(String catalog, String login, String pass) {
		this(true,null,catalog,login,pass);
	}

	private EmbeddedDataSource(boolean inMemory, URL location, String catalog, String login, String pass) {

		this.inMemory = inMemory;
		this.location = location;
		this.catalog = catalog;
		if (inMemory){
			this.url = "jdbc:hsqldb:mem:" + catalog;
		} else {
			this.url = "jdbc:hsqldb:hsql://localhost/" + catalog;
		}
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


	
		if(!inMemory){
			Logging.getBook(this.getClass()).info("Starting :" + url);
			
			String[] params = new String[4];
			params[0] = "-database.0";
			params[1] = location.toString();
			params[2] = "-dbname.0";
			params[3] = catalog;

			Introspector.of(Object.class).load("org.hsqldb.Server").invokeMain(params);
		} else {
			Logging.getBook(this.getClass()).info("Starting in memory database");
		}


	}

	public void stop(){

		Logging.getBook(this.getClass()).info("Stopping :" + url);

		Connection con=null;
		try{
			try {
				con = riseConnection(this.login, this.pass);

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
		return getConnection(this.login,this.pass);
	}

	
	public Connection riseConnection(String nlogin, String npass) throws SQLException{
		return DriverManager.getConnection(this.url, nlogin,npass);
	}
	
	ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	
	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		
		Connection c = local.get();
		
		if ( c == null ){
			TransactionService ts = ServiceRegistry.getService(TransactionService.class);
			Connection con = riseConnection(nlogin,npass);
			
			if (ts.haveTransaction()){
				con.setAutoCommit(false);
		
				ts.enlistResource(new XAConnectionControl(con));
				
				c = new EmbededConnection(con);
				local.set(c);
			} else {
				con.setAutoCommit(this.isAutoCommit());
				return con;
			}
		
		}
		
		return c;
	}
	
	public static class EmbededConnection extends DelegatingConnection  {

		public EmbededConnection(Connection original) {
			super(original);
		}

		
		public void commit () throws SQLException {
			// do nothing
		}
		
		public void rollback () throws SQLException {
			// do nothing
		}
		
		public void close() throws SQLException{
			// do nothing
		}
	}
	
	public  class XAConnectionControl extends XAResourceAdapter  {

		
		private Connection connection;

		public XAConnectionControl (Connection c){
			this.connection = c;
		}
		
		@Override
		public void commit(Xid xid, boolean onePhase) throws XAException {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}

		@Override
		public void rollback(Xid xid) throws XAException {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}

		public void end(Xid xid, int flags){
			try {
				this.connection.close();
				local.set(null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}

}
