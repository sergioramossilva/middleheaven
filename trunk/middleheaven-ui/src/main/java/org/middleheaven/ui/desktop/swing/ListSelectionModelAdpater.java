package org.middleheaven.ui.desktop.swing;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.ComboBoxModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import org.middleheaven.ui.models.UISelectionModel;

public class ListSelectionModelAdpater implements ComboBoxModel, ListSelectionModel {

	private Set<ListSelectionListener> selectionListeners = new CopyOnWriteArraySet<ListSelectionListener>();
	Set<ListDataListener> dataListeners = new CopyOnWriteArraySet<ListDataListener> ();
	
	private UISelectionModel selectionModel;
	private int selectionMode;
	private boolean isAdjusting = false;
	private int leadSelectionIndex;
	private int anchorSelectionIndex;
	
	public ListSelectionModelAdpater(UISelectionModel selectionModel , int selectionMode) {
		super();
		this.selectionModel = selectionModel;
	}

	@Override
	public Object getSelectedItem() {
		return selectionModel.getElementAt(selectionModel.getMinSelectionIndex());
	}

	@Override
	public void setSelectedItem(Object anItem) {
		int index = selectionModel.indexOf(anItem);
		if (index <0){
			return;
		}
		
		this.setSelectionInterval(index, index);
	}

	@Override
	public Object getElementAt(int index) {
		return selectionModel.getElementAt(index);
	}

	@Override
	public int getSize() {
		return selectionModel.getSize();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		dataListeners.remove(l);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		dataListeners.add(l);
	}

	@Override
	public void addListSelectionListener(ListSelectionListener x) {
		selectionListeners.add(x);
	}
	
	@Override
	public void clearSelection() {
		selectionModel.clearSelection();
	}

	@Override
	public void addSelectionInterval(int index0, int index1) {
		// no-op
	}
	
	@Override
	public void insertIndexInterval(int index, int length, boolean before) {
		// no-op
	}
	
	@Override
	public void removeIndexInterval(int index0, int index1) {
		// no-op
	}
	
	@Override
	public void removeSelectionInterval(int start, int end) {
		this.selectionModel.removeSelectionInterval(start,end);
	}
	
	@Override
	public void setSelectionInterval(int start, int end) {
		this.selectionModel.setSelectionInterval(start,end);
	}

	@Override
	public int getAnchorSelectionIndex() {
		return anchorSelectionIndex;
	}

	@Override
	public int getLeadSelectionIndex() {
		return leadSelectionIndex;
	}

	@Override
	public int getMaxSelectionIndex() {
		return selectionModel.getMaxSelectionIndex();
	}

	@Override
	public int getMinSelectionIndex() {
		return selectionModel.getMinSelectionIndex();
	}

	@Override
	public int getSelectionMode() {
		return selectionMode;
	}
	
	@Override
	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
	}
	
	@Override
	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}



	@Override
	public boolean isSelectedIndex(int index) {
		return selectionModel.isSelectedIndex(index);
	}

	@Override
	public boolean isSelectionEmpty() {
		return selectionModel.isSelectionEmpty();
	}


	@Override
	public void removeListSelectionListener(ListSelectionListener x) {
		this.selectionListeners.remove(x);
	}



	@Override
	public void setAnchorSelectionIndex(int index) {
		this.anchorSelectionIndex = index;
	}

	@Override
	public void setLeadSelectionIndex(int index) {
		this.leadSelectionIndex = index;
	}

	@Override
	public void setValueIsAdjusting(boolean valueIsAdjusting) {
		this.isAdjusting = valueIsAdjusting;
	}
}
