package org.middleheaven.storage.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategyRestrictionException;

class FastlaneQuery<T> implements Query<T> {

	private ResultSet rs;
	public FastlaneQuery(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public long count() {
		throw new ReadStrategyRestrictionException();
	}

	@Override
	public T find() {
		throw new ReadStrategyRestrictionException();
	}

	@Override
	public Collection<T> list() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void finalize(){
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
