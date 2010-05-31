package org.middleheaven.ui.web;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.models.AbstractSelectionModel;

public class HtmlSelectionModel extends AbstractSelectionModel {

	Map<String, String> formats = new HashMap<String, String>();
	
	public HtmlSelectionModel (){
		
	}

	@Override
	public <T> Formatter<T> getFormater() {
		// TODO implement AbstractSelectionModel.getFormater
		return new Formatter<T>(){

			@Override
			public String format(T object) {
				return formats.get(object);
			}

			@Override
			public T parse(String stringValue) {
				return null;
			}
			
		};
	}
	
	public void addElement(String caption , String value, boolean isSelected){
		SelectionItem item = new SelectionItem(value);
		item.setSelected(isSelected);
		this.addSelectionItem(item);
		
		formats.put(value, caption);
	}

}
