/**
 * 
 */
package org.middleheaven.storage.testdomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.middleheaven.domain.model.annotations.Id;
import org.middleheaven.domain.model.annotations.OneToMany;
import org.middleheaven.storage.model.Column;
import org.middleheaven.storage.model.Table;
import org.middleheaven.util.identity.Identity;

@Table("tablec")
public class TestSubject {
	
	@Id
	private Id identity;  
	
	private Collection<TestFamillyMember> familly = new ArrayList<TestFamillyMember>();

	@Column(persistable=false)
	private String transientField;
	
	@Column("active")
	private boolean active;
	private String name;
	
	@Column("birth")
	private Date birthdate;

	private int number;
	
	public Id getIdentity() {
		return identity;
	}

	public void setIdentity(Id identity) {
		this.identity = identity;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public TestSubject() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(target = TestFamillyMember.class)
	public Collection<TestFamillyMember> getTestFamillyMembers(){
		return Collections.unmodifiableCollection(this.familly);
	}
	
	// the name follwoing add is not the same as the add object class
	public void addFamillyMember(TestFamillyMember member){
		this.familly.add(member);
		member.setParent(this);
	}
	
	public void removeFamillyMember(TestFamillyMember member){
		if (member.getParent().equals(this)){
			this.familly.remove(member);
		}
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return this.number;
	}
}