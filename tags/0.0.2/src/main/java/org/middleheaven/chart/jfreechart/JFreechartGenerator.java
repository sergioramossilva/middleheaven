package org.middleheaven.chart.jfreechart;

import org.jfree.chart.JFreeChart;
import org.middleheaven.chart.ChartEngineBasedGenerator;

public abstract class JFreechartGenerator extends ChartEngineBasedGenerator<JFreeChart> {

	public JFreechartGenerator() {
		super(new JFreeChartEngine());
	}

}
