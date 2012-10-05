package org.middleheaven.ui;

import java.io.Serializable;
import java.util.List;

import org.middleheaven.util.StringUtils;



/**
 * User Interface Environment.
 * An application can have more than one User Interface Environment. 
 * Different types of User Interface Environment are used to interact with different categories of users.
 * Human uses interact via a Console, Desktop or Browser environment while other application interact via a 
 * Webservice environment.
 *  
 *
 */
public abstract class UIEnvironment implements Serializable{

	private static final long serialVersionUID = -7807493488204561383L;

	private UIClient client;
	private UIEnvironmentType type;
	private String name;
	
	public UIEnvironment(UIEnvironmentType type) {
		this.type = type;
	}
	
	public UIEnvironmentType getType(){
		return type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public UIClient getClient(){
		return client;
	}
	
	public void setClient(UIClient client){
		if (!accept(client.getClass())){
			throw new UIException(client.getClass().getName() + " cannot be added to " + this.getClass().getName());
		} 
		this.client = client;
	}

	
	public boolean equals(Object other){
		return other instanceof UIEnvironment 
		&& ((UIEnvironment)other).name.equals(this.name)
		&& ((UIEnvironment)other).type.equals(this.type);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	
	public String toString(){
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(this.getClient().toString()).append("\n");
		
		toString(builder, this.getClient().getChildrenComponents(), 1);
		
		return builder.toString();
	}
	
	private void toString(StringBuilder builder, List<UIComponent> children, int ident){
		
		String tabs = StringUtils.repeat("\t", ident);
		
		for (UIComponent c : children){
			
			builder.append(tabs).append(c.toString()).append("\n");
			
			toString(builder, c.getChildrenComponents(), ident+1);
		}
		
	}
	
	protected abstract boolean accept(Class<? extends UIClient> type);
	
}
