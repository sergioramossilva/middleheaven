/**
 * 
 */
package org.middleheaven.persistance;

/**
 * 
 */
public class DataStoreName {

	
	private String name;
	
	/**
	 * store:/provider/name
	 * 
	 * @param provider
	 * @param name
	 * @return
	 */
	public static DataStoreName name(String name) {
		return new DataStoreName(name);
	}
	
	private DataStoreName(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
