package org.middleheaven.persistance.db.datasource;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractDataSource  implements DataSource{

	private PrintWriter logWriter;
	private int timeOut = 30; // secounds
	private boolean autoCommit = true;
	
    public void setAutoCommit(boolean autoCommit){
    	this.autoCommit = autoCommit;
    }
    
    protected boolean isAutoCommit(){
    	return this.autoCommit;
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
	    return timeOut ;
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
}
