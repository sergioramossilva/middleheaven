package org.middleheaven.chart;

public class DataSets {

	public static Dataset singleSerie(Series data) {
		return new SingleDataset(data);
	}

	
	private static class SingleDataset extends AbstractDataset{

		Series data;
		
		public SingleDataset(Series data) {
			super();
			this.data = data;
			this.data.addSeriesListener(this);
		}

		
		@Override
		public Series getSerie(int index) {
			return data;
		}

		
		@Override
		public int size() {
			return 1;
		}

	
	}
}
