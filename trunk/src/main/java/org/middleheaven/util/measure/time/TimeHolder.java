package org.middleheaven.util.measure.time;

/**
 * Holds information on time (hour, minute, second. milisecond)
 * @author Sergio Taborda
 *
 */
public interface TimeHolder {

	
	public int hour();
	public int minute();
	public int second();
	public long miliseconds();
}
