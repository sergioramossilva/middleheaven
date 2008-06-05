package org.middleheaven.events;

public interface EventListener {

	
	public boolean isListenable(Event event);
	
	public void onEvent(Event event);
}
