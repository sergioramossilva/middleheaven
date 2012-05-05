/**
 * 
 */
package org.middleheaven.core.dependency;

/**
 * 
 */
public class DependencyGraphNode {

	
	private String name;
	private DependencyInicilizer ini;

	public DependencyGraphNode (String name, DependencyInicilizer ini){
		this.name = name;
		this.ini = ini;
	}
	
	public String toString(){
		return name;
	}
	
	public void inicialize(){
		ini.inicialize(this);
	}
}
