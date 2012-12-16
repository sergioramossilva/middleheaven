/**
 * 
 */
package org.middleheaven.core.reflection;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SomeBean {
	
	private List list;

	// to confuse inspector
	public void setList(ArrayList list){
		this.list = list;
	}
	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	
	public void setName(CharSequence c) {
	
	}
	
	public void setName(String c) {
		
	}

}
