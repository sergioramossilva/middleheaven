package org.middleheaven.ui;


public interface UIService {


	public UIEnvironment getUIEnvironment(String envID);
	public void registerEnvironment(UIEnvironment env);
	public UIClient getUIClient(UIEnvironmentType type, String name);
	public void setUIClient(UIEnvironmentType type, String name,UIClient client);

}
