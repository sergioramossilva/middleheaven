package org.middleheaven.chart;

/**
 * General contract for a class that generates charts
 *
 */
public interface ChartGenerator {

	public Chart generateChart(ChartContext context) throws ChartException;
}
