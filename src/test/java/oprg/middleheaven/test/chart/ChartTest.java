package oprg.middleheaven.test.chart;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.middleheaven.chart.Chart;
import org.middleheaven.chart.ChartEngine;
import org.middleheaven.chart.ChartLayout;
import org.middleheaven.chart.ChartType;
import org.middleheaven.chart.HistogramSeries;


public class ChartTest {

	
	public static void main(String ... args){
		ChartTest r = new ChartTest();
		r.testHistogramChart();
	}
	
	@Test
	public void testHistogramChart(){
		
		ChartEngine engine = new JFreeChartEngine();
		
		
		HistogramSeries<String, Integer> hs = new HistogramSeries<String, Integer>("World Population (per year)");
		hs.add("1990", 12345);
		hs.add("1991", 12234);
		hs.add("1992", 1254);
		
		ChartLayout layout = ChartLayout.layout(ChartType.PIE , "World Population (per year)");
		
		Chart chart = engine.createChart(hs, layout);
		
		assertTrue (chart.getChartObject()!=null);
		
		// show
		show(chart);
		
	}
	
	private static void show(final Chart chart){
		ChartTestFrame.forChart(chart).setVisible(true);
	}
	
	private static class ChartTestFrame extends JFrame{
		
		public static ChartTestFrame forChart(Chart chart){
			return new ChartTestFrame(chart);
		}
		
		public ChartTestFrame(Chart chart){
			 final ChartPanel chartPanel = new ChartPanel((JFreeChart)chart.getChartObject());
		     chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		     setContentPane(chartPanel);
		     this.pack();
		}
	}
}
