package org.middleheaven.chart;

public interface ChartEngine {

	public Chart createChart(Series data , ChartLayout layout);
	public Chart createChart(Dataset data , ChartLayout layout);
}
