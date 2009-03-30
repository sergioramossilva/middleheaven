package org.middleheaven.storage.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.middleheaven.storage.PersistableState;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.util.identity.Identity;

public class ResultSetStorable implements Storable {

	private ResultSet rs;
	private StorableEntityModel model;
	
	public ResultSetStorable(ResultSet rs, StorableEntityModel model) {
		this.rs = rs;
		this.model = model;
	}

	@Override
	public Identity getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentity(Identity id) {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public void setPersistableState(PersistableState state) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Class<?> getPersistableClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PersistableState getPersistableState() {
		return PersistableState.RETRIVED;
	}

	@Override
	public Object getFieldValue(StorableFieldModel model) {
		try {
			return rs.getObject(model.getHardName().getName());
		} catch (SQLException e) {
			throw new StorageException(e); 
		}
	}

	@Override
	public void setFieldValue(StorableFieldModel model, Object fieldValue) {
		throw new UnsupportedOperationException();
	}

}
