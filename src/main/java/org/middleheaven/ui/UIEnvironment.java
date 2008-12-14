package org.middleheaven.ui;

import java.io.Serializable;


/**
 * User Interface Environment.
 * An application can have more than one User Interface Environment. 
 * Different types of User Interface Environment are used to interact with different categories of users.
 * Human uses interact via a Console, Desktop or Browser environment while other application interact via a 
 * Webservice environment.
 *  
 *
 */
public class UIEnvironment implements Serializable{

	private static final long serialVersionUID = -7807493488204561383L;
	
	private UIClient client;
	private UIEnvironmentType type;
	private String name;
	
	public UIClient getUIClient(){
		return this.client;
	}
	
	public void setUIClient(UIClient client){
		this.client = client;
	}
	
	public UIEnvironmentType getType(){
		return this.type;
	}
	
	public void setType(UIEnvironmentType type){
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public boolean equals(Object other){
		return other instanceof UIEnvironment 
		&& ((UIEnvironment)other).name.equals(this.name)
		&& ((UIEnvironment)other).type.equals(this.type);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
}
