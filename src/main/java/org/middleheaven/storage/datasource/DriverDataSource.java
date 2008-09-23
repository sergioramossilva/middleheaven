package org.middleheaven.storage.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DriverDataSource implements DataSource {

	private String URL;
	private String login;
	private String pass;
	  
	private int timeOut = 30; // seconds
	private PrintWriter logWriter = null;
	 
	private boolean autoCommit = true;
	public DriverDataSource(){}

    public DriverDataSource(String driver,String url,String username,String password){

    	this.URL = url;
        this.login = username;
        this.pass = password;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DriverNotFoundException("Driver " + driver + " was not found.");
        }
        
    }
    
    public void setAutoCommit(boolean autoCommit){
    	this.autoCommit = autoCommit;
    }
    
	@Override
	public Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(URL, login, pass);
		con.setAutoCommit(autoCommit);
		return con;
	}

	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		
		Connection con = DriverManager.getConnection(URL, nlogin, npass);
		con.setAutoCommit(autoCommit);
		return con;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}
	
	@Override
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		this.logWriter = logWriter;
	}
	
	@Override
	public int getLoginTimeout() throws SQLException {
	    return timeOut;
	}

	@Override
	public void setLoginTimeout(int timeOut) throws SQLException {
		 this.timeOut = timeOut;  
	}
	
	@Override
	public boolean isWrapperFor(Class<?> other) throws SQLException {
		return this.getClass().isAssignableFrom(other);
	}

	@Override
	public <T> T unwrap(Class<T> other) throws SQLException {
		if (this.getClass().isAssignableFrom(other)){
			return (T)other.getClass().cast(this);
		}
		throw new SQLException(this.getClass().getName() + " is not a wrapper for " + other.getName());
	}

	protected String getURL() {
		return URL;
	}

	protected void setURL(String url) {
		URL = url;
	}

	protected String getLogin() {
		return login;
	}

	protected void setLogin(String login) {
		this.login = login;
	}

	protected String getPassword() {
		return pass;
	}

	protected void setPassword(String pass) {
		this.pass = pass;
	}

}
