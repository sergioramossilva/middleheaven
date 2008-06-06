package org.middleheaven.chart;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



public abstract class AbstractDataset implements Dataset {
	List<DatasetListener> listeners = new CopyOnWriteArrayList<DatasetListener>();

	public AbstractDataset(){
	}
	
	protected synchronized void fireChangeEvent(){
		DatasetChangeEvent event = new DatasetChangeEvent(this);
		for (DatasetListener l : listeners){
			l.onChange(event);
		}
		event = null;
	}
	
	@Override
	public void addDatasetListener(DatasetListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeDatasetListener(DatasetListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void onChange(SeriesChangeEvent event) {
		this.fireChangeEvent();
	}

}
