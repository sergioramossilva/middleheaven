package org.middleheaven.chart;

public class SeriesChangeEvent {

	private Series series;

	public SeriesChangeEvent(Series series) {
		this.series = series;
	}

	public Series getSeries() {
		return series;
	}
	
}
