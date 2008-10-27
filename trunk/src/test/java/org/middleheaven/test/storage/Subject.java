/**
 * 
 */
package org.middleheaven.test.storage;

import java.util.Date;

import org.middleheaven.storage.model.Column;
import org.middleheaven.storage.model.Table;

@Table("tablec")
public class Subject {
	
	@Column(persistable=false)
	private String transientField;
	
	@Column("ativo")
	private boolean active;
	private String name;
	
	@Column("aniversario")
	private Date birthdate;
	
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