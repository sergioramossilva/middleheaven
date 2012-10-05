/**
 * 
 */
package org.middleheaven.core.dependency;

/**
 * 
 */
public class DependencyGraphNode {

	
	private Object object;
	private DependencyInicilizer ini;

	public DependencyGraphNode (Object object, DependencyInicilizer ini){
		this.object = object;
		this.ini = ini;
	}
	
	public String toString(){
		return object.toString();
	}
	
	public void inicialize(){
		ini.inicialize(this);
	}
	
	public boolean equals(Object other) {
		return (other instanceof DependencyGraphNode) && ((DependencyGraphNode) other).object.equals(this.object);
	}
	
	public int hashCode(){
		return object.hashCode();
	}

	/**
	 * 
	 */
	public Object getObject() {
		return object;
	}
}
