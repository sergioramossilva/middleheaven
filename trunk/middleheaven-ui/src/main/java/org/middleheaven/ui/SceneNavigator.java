/**
 * 
 */
package org.middleheaven.ui;


/**
 * 
 */
public interface SceneNavigator {

	/**
	 * Make the component visible to the user.
	 * @param component
	 */
	public abstract void show(UIComponent component);

	/**
	 * Turn the component not visible, and then dispose of the component if possible, i.e. remove it from memory. 
	 * @param component
	 */
	public abstract void dispose(UIComponent component);
}
