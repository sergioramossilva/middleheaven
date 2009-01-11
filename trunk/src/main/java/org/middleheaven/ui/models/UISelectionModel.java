package org.middleheaven.ui.models;


public interface UISelectionModel extends UIInputModel {

	public Object getElementAt(int index);

	public int getSize();

	public void clearSelection();

	public boolean isSelectedIndex(int index);

	public boolean isSelectionEmpty();

	public int getMaxSelectionIndex();

	public int getMinSelectionIndex();

	public void setSelectionInterval(int start, int end);

	public void removeSelectionInterval(int start, int end);

	public int indexOf(Object anItem);

}
