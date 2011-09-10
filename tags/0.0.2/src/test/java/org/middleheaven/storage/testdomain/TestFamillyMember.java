package org.middleheaven.storage.testdomain;

import org.middleheaven.domain.model.annotations.ManyToOne;


public class TestFamillyMember {

	
	private TestSubject parent;
	private String name;
	private TestRelation relation;
	
	@ManyToOne
	public TestSubject getParent() {
		return parent;
	}
	
	public void setParent(TestSubject parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	public TestRelation getRelation() {
		return relation;
	}
	
	public void setRelation(TestRelation relation) {
		this.relation = relation;
	}
	
	
}
