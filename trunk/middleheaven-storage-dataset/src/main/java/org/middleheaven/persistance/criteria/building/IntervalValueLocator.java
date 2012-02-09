/**
 * 
 */
package org.middleheaven.persistance.criteria.building;


/**
 *
 */
public class IntervalValueLocator extends ColumnValueLocator {


	private ColumnValueLocator start;
	private ColumnValueLocator end;

	public IntervalValueLocator (ColumnValueLocator start, ColumnValueLocator end){
		this.start = start;
		this.end = end;
	}

	public ColumnValueLocator getStart() {
		return start;
	}

	public ColumnValueLocator getEnd() {
		return end;
	}

	
	
	
	
}
