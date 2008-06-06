package org.middleheaven.chart;

public  class DatasetChangeEvent {

	
	private Dataset dataset;

	public DatasetChangeEvent(Dataset dataset) {
		this.dataset = dataset;
	}

	public Dataset getDataset() {
		return dataset;
	}
	
}
