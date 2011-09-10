/**
 * 
 */
package org.middleheaven.process;

public interface Attribute {
	
	public String getName();
	public Object getValue();
	public <T> T getValue(Class<T> type);
	
}