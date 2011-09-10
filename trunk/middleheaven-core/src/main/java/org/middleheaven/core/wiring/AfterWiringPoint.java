package org.middleheaven.core.wiring;

/**
 * After creation wiring point. The framework will use this Wiring point to bind other objects.
 */
public interface AfterWiringPoint extends WiringPoint {

	/**
	 * Indicates if this wiring is required. An exception will be thrown by the Wiring service if the wiring cannot be satisfied.
	 * @return <code>true</code> if this wiring is required. 
	 */
	public boolean isRequired();
	
	/**
	 * Executes the wiring on the <code>object</code>.
	 * 
	 * @param <T> the class of the object that will be injected with this {@link AfterWiringPoint}.
	 * @param binder the binder being used.
	 * @param object the object that will be injected with this {@link AfterWiringPoint}.
	 * @return the object after wiring.
	 */
	public <T> T writeAtPoint(EditableBinder binder, T object);
	
}
