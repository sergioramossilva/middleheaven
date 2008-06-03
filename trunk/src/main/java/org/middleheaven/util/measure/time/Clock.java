package org.middleheaven.util.measure.time;

import java.util.TimeZone;

/**
 * @author  Sergio M.M. Taborda
 */
public interface Clock {

	public TimePoint now();

	public TimeZone getTimeZone();
}
