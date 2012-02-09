package org.middleheaven.persistance.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataRowStream;
import org.middleheaven.persistance.DataRowStreamException;

/**
 * 
 */
public final class ListDataRowStream implements DataRowStream {

	private final List<DataRow> rows;
	private Iterator<DataRow> it;
	
	public ListDataRowStream(){
		rows = new LinkedList<DataRow>();
	}
	
	public ListDataRowStream(int maxCount) {
		rows = new ArrayList<DataRow>(maxCount);
	}


	public void addRow(DataRow row) {
		this.rows.add(row);
	}
	
	@Override
	public boolean next() throws DataRowStreamException {
		if (it == null){
			it = rows.iterator();
		}
		
		return it.hasNext();
	}

	@Override
	public DataRow currentRow() throws DataRowStreamException {
		return it.next();
	}

	@Override
	public void close() throws DataRowStreamException {
		// no-op
	}

}
