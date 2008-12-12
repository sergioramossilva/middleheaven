/**
 * 
 */
package org.middleheaven.test.storage;

import java.util.Date;

import org.middleheaven.domain.annotations.Key;
import org.middleheaven.storage.model.Column;
import org.middleheaven.storage.model.Table;
import org.middleheaven.util.identity.Identity;

@Table("tablec")
public class Subject {
	
	@Key
	private Identity identity;  
	

	@Column(persistable=false)
	private String transientField;
	
	@Column("ativo")
	private boolean active;
	private String name;
	
	@Column("aniversario")
	private Date birthdate;
	
	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	protected Date getBirthdate() {
		return birthdate;
	}

	protected void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Subject() {
		super();
	}
	
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
}