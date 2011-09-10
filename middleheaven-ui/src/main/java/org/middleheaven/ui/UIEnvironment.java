package org.middleheaven.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.rendering.RenderKit;



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

	private Collection<UIClient> clients = new LinkedList<UIClient>();
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
	
	public Collection<UIClient> getClients(){
		return Collections.unmodifiableCollection(this.clients);
	}
	
	public void addClient(UIClient client){
		if (!accept(client.getClass())){
			throw new UIException(client.getClass().getName() + " cannot be added to " + this.getClass().getName());
		} 
		this.clients.add(client);
	}

	public void removeClient(UIClient client){
		this.clients.remove(client);
	}
	
	public boolean equals(Object other){
		return other instanceof UIEnvironment 
		&& ((UIEnvironment)other).name.equals(this.name)
		&& ((UIEnvironment)other).type.equals(this.type);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	protected abstract boolean accept(Class<? extends UIClient> type);
	
}
