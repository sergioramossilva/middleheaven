package org.middleheaven.ui.models;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.util.collections.Interval;

public class ListSelectionModel extends AbstractUIModel implements UISelectionModel{

	List<SelectionItem> elements = new ArrayList<SelectionItem>();
	
	private int maxIndex = -1;
	private int minIndex = -1;
	
	private class SelectionItem {
		Object obj;
		boolean isSelected = false;
		
		public SelectionItem(Object obj){
			this.obj = obj;
		}
		
		public int hashCode(){
			return obj ==null ? 0 : obj.hashCode();
		}
		
		public boolean equals(Object other){
			return other instanceof SelectionItem && ((SelectionItem)other).obj.equals(obj);
		}
	}
	
	public ListSelectionModel(){
		
	}
	
	public ListSelectionModel(List<?> elements){
		for (Object obj : elements){
			this.elements.add(new SelectionItem(obj));
		}
	}
	
	
	public void addElement(Object obj){
		this.elements.add(new SelectionItem(obj));
	}
	
	public void removeElement(Object obj){
		this.elements.remove(new SelectionItem(obj));
	}
	
	@Override
	public Object getElementAt(int index) {
		return index < 0 || index > elements.size() -1 ? null :  elements.get(index).obj;
	}

	@Override
	public int getSize() {
		return elements.size();
	}


	@Override
	public <T> Formatter<T> getFormater() {
		// TODO implement UIOutputModel.getFormater
		return null;
	}

	@Override
	public Object getValue() {
		if (this.maxIndex == this.minIndex){
			return new Object[]{this.getElementAt(maxIndex)};
		} else {
			Object[] res = new Object[maxIndex - minIndex+1];
			int j =0;
			for (int i = this.getMinSelectionIndex() ; i <= this.getMaxSelectionIndex(); i++, j++){
				if (this.isSelectedIndex(i)){
					res[j] = this.getElementAt(i);
				}
			}
			return res;
		}
	}


	@Override
	public void clearSelection() {
		for (SelectionItem item : this.elements){
			item.isSelected = false;
		}
	}

	@Override
	public boolean isSelectedIndex(int index) {
		return this.elements.get(index).isSelected;
	}

	@Override
	public boolean isSelectionEmpty() {
		for (SelectionItem item : this.elements){
			if (item.isSelected){
				return false;
			}
		}
		return true;
	}

	@Override
	public int getMaxSelectionIndex() {
		return maxIndex;
	}

	@Override
	public int getMinSelectionIndex() {
		return minIndex;
	}

	@Override
	public int indexOf(Object anItem) {
		return elements.indexOf(new SelectionItem(anItem));
	}

	@Override
	public void removeSelectionInterval(int start, int end) {
		Interval<Integer> interval = Interval.between(minIndex, maxIndex).intersection(Interval.between(start,end));
		
		for (int i = interval.start() ; i <= interval.end() ; i++){
			elements.get(i).isSelected = false;
		}
		
		this.maxIndex = Math.max( maxIndex,  interval.end());
		this.minIndex = Math.max( minIndex,  interval.start());
	}

	@Override
	public void setSelectionInterval(int start, int end) {
		this.minIndex = minIndex < 0 ? start : Math.min(this.minIndex, start);
		this.maxIndex = maxIndex < 0 ? end : Math.max(this.maxIndex, end);;
		
		if (this.maxIndex == this.minIndex){
			elements.get(maxIndex).isSelected = true;
		} else {
			for (int i = minIndex ; i <= maxIndex ; i++){
				elements.get(i).isSelected = true;
			}
		}
	}



}
