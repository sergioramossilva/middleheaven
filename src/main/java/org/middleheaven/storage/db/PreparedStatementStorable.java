package org.middleheaven.storage.db;

import java.sql.PreparedStatement;

import org.middleheaven.storage.Storable;

public class PreparedStatementStorable {

	PreparedStatement ps;
	public PreparedStatementStorable(PreparedStatement ps) {
		this.ps = ps; 
		 
	}
	public void copy(Storable s) {
		// TODO Auto-generated method stub
		
	}

}
