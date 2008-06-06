package oprg.middleheaven.test.chart;

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
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.middleheaven.chart.AbstractChartEngine;
import org.middleheaven.chart.Chart;
import org.middleheaven.chart.ChartLayout;
import org.middleheaven.chart.ChartOrientation;
import org.middleheaven.chart.ChartType;
import org.middleheaven.chart.Dataset;
import org.middleheaven.chart.Series;

public class JFreeChartEngine extends AbstractChartEngine  {


	private static PlotOrientation orientationFor(ChartOrientation orientation){
		return orientation.equals(ChartOrientation.VERTICAL) ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL ;
	}


	private static <T> T createJFreeDataSet(Class<T> dataSetType, Dataset dataset,ChartLayout layout){

		if (dataSetType.equals(CategoryDataset.class) ){
			final double[][] data = new double[][]
			                                     {{10.0, 4.0, 15.0, 14.0},
					{-5.0, -7.0, 14.0, -3.0},
					{6.0, 17.0, -12.0, 7.0},
					{7.0, 15.0, 11.0, 0.0},
					{-8.0, -6.0, 10.0, -9.0},
					{9.0, 8.0, 0.0, 6.0},
					{-10.0, 9.0, 7.0, 7.0},
					{11.0, 13.0, 9.0, 9.0},
					{-3.0, 7.0, 11.0, -10.0}};

			return (T)DatasetUtilities.createCategoryDataset("Series ", "Category ", data);

		} else if (dataSetType.equals(PieDataset.class)){ 
			DefaultPieDataset jdataset = new DefaultPieDataset();
			
			for (int s =0 ; s < dataset.size(); s++){
				Series  series = dataset.getSerie(s);
				for (int i = 0 ; i < series.size(); i++){
					Comparable c = series.getValue(0, i);
					Comparable x = series.getValue(1, i);
					jdataset.setValue(c, new BigDecimal(x.toString()));
				}
			}
			
			return (T)jdataset;
		}else {
			throw new UnsupportedOperationException (dataSetType + " is not supported");
		}



	}
	@Override
	public Chart createChart(Dataset data, ChartLayout layout) {


		if (layout.getType().equals(ChartType.BARS)){
			final CategoryDataset dataset = createJFreeDataSet( CategoryDataset.class, data, layout);


			final JFreeChart chart;
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
						true,                     // include legend
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

			return new Chart(layout.getTitle(),chart);
		} if (layout.getType().equals(ChartType.PIE)){
			final PieDataset dataset = createJFreeDataSet( PieDataset.class, data, layout);


			JFreeChart chart = ChartFactory.createPieChart(
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
			return new Chart(layout.getTitle(),chart);
		}else {
			throw new UnsupportedOperationException (layout.getType() + " is not supported");
		}
	}


}
