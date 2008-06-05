/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;


public class DependencySupport implements Dependency{

   
	private boolean required= false;
    private Class<?> dependencyClass;
   
    
    public DependencySupport(Class<?> dependencyClass){
    	this.dependencyClass = dependencyClass;
    }
    
    public DependencySupport(Class<?> dependencyClass, boolean required){
    	this.dependencyClass = dependencyClass;
    	this.required = required;
    }
    
    public void setRequired(boolean value){
        this.required = value;
    }
    
    public boolean isRequired() {
        return this.required;
    }

	@Override
	public Class<?> getDependencyClass() {
		return dependencyClass;
	}

}
