package org.middleheaven.ui;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.rendering.RenderKit;


@Service
public interface UIService {

	
	/**
	 * Retrieves a previously registered user interface environment by its name;
	 * @param envName name of the environment
	 * @return
	 */
	public UIEnvironment getUIEnvironment(String envName);
	
	
	public UIEnvironment getUIEnvironment(UIEnvironmentType type);
	
	
	public Rendering<UIClient> getUIClientRendering(UIEnvironmentType type);

	public Rendering<UIClient> getUIClientRendering(UIEnvironmentType type, AttributeContext context);
	/**
	 * Registers an user interface environment
	 * @param env the user interface environment to register
	 * 
	 * @throws  UIException exception is thrown if a default type is already set for the type
	 */
	public void registerEnvironment(UIEnvironment env, RenderKit renderKit, AttributeContext context) throws UIException;
	
	public void unRegisterEnvironment(UIEnvironment env);
	
}
