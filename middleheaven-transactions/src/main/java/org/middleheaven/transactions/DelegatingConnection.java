/**
 * 
 */
package org.middleheaven.transactions;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

public class DelegatingConnection implements Connection {

    Connection original;

	public DelegatingConnection (Connection original){
		this.original = original;
	}

	public void clearWarnings() throws SQLException {
		original.clearWarnings();
	}

	public void close() throws SQLException {
		// no-op
	}

	public void commit() throws SQLException {
		//no-op
	}
	
	public void rollback() throws SQLException {
		//no-op
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		//no-op
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		original.setAutoCommit(autoCommit);
	}


	public Array createArrayOf(String typeName, Object[] elements)throws SQLException {
		return original.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return original.createBlob();
	}

	public Clob createClob() throws SQLException {
		return original.createClob();
	}

	public NClob createNClob() throws SQLException {
		return original.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return original.createSQLXML();
	}

	public Statement createStatement() throws SQLException {
		return original.createStatement();
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return original.createStatement(resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return original.createStatement(resultSetType, resultSetConcurrency);
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return original.createStruct(typeName, attributes);
	}

	public boolean getAutoCommit() throws SQLException {
		return original.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return original.getCatalog();
	}

	public Properties getClientInfo() throws SQLException {
		return original.getClientInfo();
	}

	public String getClientInfo(String name) throws SQLException {
		return original.getClientInfo(name);
	}

	public int getHoldability() throws SQLException {
		return original.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return original.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return original.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return original.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return original.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return original.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return original.isReadOnly();
	}

	public boolean isValid(int timeout) throws SQLException {
		return original.isValid(timeout);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return original.isWrapperFor(iface);
	}

	public String nativeSQL(final String sql) throws SQLException {
		return original.nativeSQL(sql);
	}

	public CallableStatement prepareCall(final String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return original.prepareCall(sql, resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	public CallableStatement prepareCall(final String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return original.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(final String sql) throws SQLException {
		return original.prepareCall(sql);
	}

	public PreparedStatement prepareStatement(final String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return original.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(final String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return original.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(final String sql, int autoGeneratedKeys)
			throws SQLException {
		return original.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return original.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return original.prepareStatement(sql, columnNames);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return original.prepareStatement(sql);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		original.releaseSavepoint(savepoint);
	}


	public void setCatalog(String catalog) throws SQLException {
		original.setCatalog(catalog);
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		original.setClientInfo(properties);
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		original.setClientInfo(name, value);
	}

	public void setHoldability(int holdability) throws SQLException {
		original.setHoldability(holdability);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		original.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return original.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return original.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		original.setTransactionIsolation(level);
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		original.setTypeMap(map);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return original.unwrap(iface);
	}
	
	
}