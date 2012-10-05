package org.middleheaven.quantity.time;

import org.middleheaven.util.Incrementor;

public class CalendarDateIncrementor implements Incrementor<CalendarDate> {

    ElapsedTime elapsedTime;
	public CalendarDateIncrementor(ElapsedTime elapsedTime){
    	 this.elapsedTime = elapsedTime;
	}

	@Override
	public CalendarDate increment(CalendarDate date) {
		return date.plus(elapsedTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<CalendarDate> reverse() {
		return new CalendarDateIncrementor(this.elapsedTime.negate());
	}

}
