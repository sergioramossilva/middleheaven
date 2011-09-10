package org.middleheaven.ui;

import java.util.Collection;


public interface UIService {

	public Collection<UIEnvironment> getUIEnvironments(UIEnvironmentType type);
	public UIEnvironment getUIEnvironment(String name);
	public UIEnvironment getDefaultUIEnvironment(UIEnvironmentType type);

	public void registerEnvironment(UIEnvironment env, boolean isDefault);
	public void unRegisterEnvironment(UIEnvironment env);
	
}
