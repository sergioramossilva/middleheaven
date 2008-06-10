package org.middleheaven.storage.db;

import java.sql.ResultSet;

import org.middleheaven.storage.PersistableState;
import org.middleheaven.storage.Storable;

public class ResultSetStorable implements Storable {

	private ResultSet rs;
	public ResultSetStorable(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getPersistableClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistableState getPersistableState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersistableState(PersistableState state) {
		// TODO Auto-generated method stub

	}

}
