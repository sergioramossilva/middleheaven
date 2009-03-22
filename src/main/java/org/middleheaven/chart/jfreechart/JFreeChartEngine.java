package org.middleheaven.chart.jfreechart;

import java.awt.Font;
import java.math.BigDecimal;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.middleheaven.chart.AbstractChartEngine;
import org.middleheaven.chart.Chart;
import org.middleheaven.chart.ChartLayout;
import org.middleheaven.chart.ChartOrientation;
import org.middleheaven.chart.ChartType;
import org.middleheaven.chart.Dataset;
import org.middleheaven.chart.DatasetChangeEvent;
import org.middleheaven.chart.DatasetListener;
import org.middleheaven.chart.Series;

public class JFreeChartEngine extends AbstractChartEngine  {


	private static PlotOrientation orientationFor(ChartOrientation orientation){
		return orientation.equals(ChartOrientation.VERTICAL) ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL ;
	}

	private static class MyCategoryDataset extends DefaultCategoryDataset implements DatasetListener{

		public MyCategoryDataset( Dataset dataset){

			fillData(dataset);
			
			dataset.addDatasetListener(this);
		}
		
		@Override
		public void onChange(DatasetChangeEvent event) {
			this.clear();
			fillData(event.getDataset());
			this.fireDatasetChanged();
		}
		
		
		private void fillData(Dataset dataset){
			for (Series series : dataset){
				for (int i = 0 ; i < series.size(); i++){
					@SuppressWarnings("unchecked") Comparable c = series.getValue(0, i);
					@SuppressWarnings("unchecked") Comparable x = series.getValue(1, i);
					this.addValue(new BigDecimal(x.toString()), series.getName(), c);
				}
			}
		}
		
	}
	
	private static class MyXYSeriesCollection extends XYSeriesCollection implements DatasetListener{

		public MyXYSeriesCollection( Dataset dataset){

			fillData(dataset);
			
			dataset.addDatasetListener(this);
		}
		
		@Override
		public void onChange(DatasetChangeEvent event) {
			this.removeAllSeries();
			fillData(event.getDataset());
			this.fireDatasetChanged();
		}
		
		private void fillData(Dataset dataset){
			for (Series series : dataset){
				XYSeries jseries = new XYSeries(series.getName());
				for (int i = 0 ; i < series.size(); i++){
					@SuppressWarnings("unchecked") Comparable x = series.getValue(0, i);
					@SuppressWarnings("unchecked") Comparable y = series.getValue(1, i);
					jseries.add(new BigDecimal(x.toString()), new BigDecimal(y.toString()));
				}
				this.addSeries(jseries);
			}
		}
		
	}
	
	private static <T> T createJFreeDataSet(Class<T> dataSetType, Dataset dataset){

	   if (dataSetType.equals(XYSeriesCollection.class)){
			
		   return  dataSetType.cast(new MyXYSeriesCollection(dataset));
		} else if (dataSetType.equals(CategoryDataset.class) ){
			
			return dataSetType.cast(new MyCategoryDataset(dataset));

		} else if (dataSetType.equals(PieDataset.class)){ 
			DefaultPieDataset jdataset = new DefaultPieDataset();

			for (Series series : dataset){
				for (int i = 0 ; i < series.size(); i++){
					@SuppressWarnings("unchecked") Comparable c = series.getValue(0, i);
					@SuppressWarnings("unchecked") Comparable x = series.getValue(1, i);
					jdataset.setValue(c, new BigDecimal(x.toString()));
				}
			}

			return dataSetType.cast(jdataset);
		}else {
			throw new UnsupportedOperationException (dataSetType + " is not supported");
		}

	}
	@Override
	public Chart createChart(Dataset data, ChartLayout layout) {

	    JFreeChart chart;
		if (layout.getType().equals(ChartType.XY)){
			final XYSeriesCollection dataset = createJFreeDataSet( XYSeriesCollection.class, data);

			chart= ChartFactory.createXYLineChart(
					layout.getTitle(),      // chart title
					layout.getAxisName(0),               // domain axis label
					layout.getAxisName(1),                  // range axis label
					dataset,                  // data
					orientationFor(layout.getOrientation()), // orientation
					data.size()>1,                     // include legend
					true,                     // tooltips
					false                     // urls
			);
			
		} else if (layout.getType().equals(ChartType.BARS)){
			final CategoryDataset dataset = createJFreeDataSet( CategoryDataset.class, data);


			
			if (layout.is3D()){
				chart= ChartFactory.createBarChart3D(
						layout.getTitle(),      // chart title
						layout.getAxisName(0),               // domain axis label
						layout.getAxisName(1),                  // range axis label
						dataset,                  // data
						orientationFor(layout.getOrientation()), // orientation
						true,                     // include legend
						true,                     // tooltips
						false                     // urls
				);
			} else {
				chart= ChartFactory.createBarChart(
						layout.getTitle(),      // chart title
						layout.getAxisName(0),               // domain axis label
						layout.getAxisName(1),                  // range axis label
						dataset,                  // data
						orientationFor(layout.getOrientation()), // orientation
						data.size()>1,                     // include legend
						true,                     // tooltips
						false                     // urls
				);
			}

			// Styling operations
			final CategoryPlot plot = chart.getCategoryPlot();
			final CategoryAxis axis = plot.getDomainAxis();
			axis.setCategoryLabelPositions(
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0)
			);

			final BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
			renderer.setDrawBarOutline(false);

	
		} else  if (layout.getType().equals(ChartType.PIE)){
			final PieDataset dataset = createJFreeDataSet( PieDataset.class, data);


			chart = ChartFactory.createPieChart(
					layout.getTitle(),  // chart title
					dataset,             // data
					true,               // include legend
					true,
					false
			);
			// Styling operations
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
			plot.setNoDataMessage("No data available");
			plot.setCircular(false);
			plot.setLabelGap(0.02);
			
		}else {
			throw new UnsupportedOperationException (layout.getType() + " is not supported");
		}
		
        return new Chart(layout.getTitle(),chart);
	}


}
