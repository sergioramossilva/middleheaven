package org.middleheaven.ui.models.impl;

import java.util.List;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.models.AbstractSelectionModel;

public class ListSelectionModel extends AbstractSelectionModel {


	
	public ListSelectionModel(){
		
	}
	
	public ListSelectionModel(List<?> elements){
		for (Object obj : elements){
			addElement(obj);
		}
	}
	
	@Override
	public <T> Formatter<T> getFormater() {
		// TODO implement UIOutputModel.getFormater
		return null;
	}

	



}
