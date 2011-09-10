package org.middleheaven.chart;


public abstract class AbstractChartEngine implements ChartEngine {

	@Override
	public final Chart createChart(Series data, ChartLayout layout) {
		return createChart(DataSets.singleSerie(data), layout);
	}





}
