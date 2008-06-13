package org.middleheaven.chart;

import java.io.Serializable;

/**
 * 
 *
 * @param <R> the real chart class
 */
public abstract class ChartEngineBasedGenerator<R extends Serializable> implements ChartGenerator {

	ChartEngine engine;
	
	public ChartEngineBasedGenerator(ChartEngine engine) {
		this.engine = engine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Chart generateChart(ChartContext context) throws ChartException {
		Dataset dataset = generateChartDataset(context);
		ChartLayout layout = generateChartLayout(context);
		Chart c = engine.createChart(dataset, layout);
		return new Chart(c.getTitle() ,  modifyDesign((R)c.getChartObject(), context) );
	}

	
	public abstract Dataset generateChartDataset(ChartContext context) throws ChartException;
	
	public ChartLayout generateChartLayout(ChartContext context) throws ChartException {
		return ChartLayout.layout(ChartType.XY, context.getChartName());
	}
	
	public R modifyDesign (R chart, ChartContext context) throws ChartException{
		return chart;
	}
}
