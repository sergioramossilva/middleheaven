package org.middleheaven.events;

public interface EventListener<E> {

	public void onEvent(E event);
}
