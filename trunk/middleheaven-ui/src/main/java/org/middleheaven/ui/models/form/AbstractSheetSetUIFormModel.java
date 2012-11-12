/**
 * 
 */
package org.middleheaven.ui.models.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIActionHandler;
import org.middleheaven.ui.models.AbstractUICommandModel;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.ui.models.UIFormModel;

/**
 * Base implementation for a Sheet based {@link UIFormModel}.
 */
public abstract class AbstractSheetSetUIFormModel implements UIFormModel {

	
	private List<UIFormSheetModel> sheets = new ArrayList<UIFormSheetModel>(1);
	List<UICommandModel> comands = new ArrayList<UICommandModel>(2);
	Map<String, UIActionHandler> handlersByName = new HashMap<String, UIActionHandler>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIActionHandler getUIActionHandler(String commandName) {
		return handlersByName.get(commandName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UICommandModel> getActions() {
		return Collections.unmodifiableList(comands);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIFormSheetModel> getFormSheets() {
		return Collections.unmodifiableList(sheets);
	}
	
	protected UIFormSheetBuilder newSheet(String name){
		final UIFormSheetModel sheet = new UIFormSheetModel(name);
		sheet.setTabOrder(this.sheets.size()+1);
		
		this.sheets.add(sheet);
		
		return new UIFormSheetBuilder(sheet);
	}
	
	protected UIFormSheetBuilder addSheet(String name, TextLocalizable caption){
		final UIFormSheetModel sheet = new UIFormSheetModel(name);
		sheet.setTabOrder(this.sheets.size()+1);
		sheet.setCaption(caption);
		
		this.sheets.add(sheet);
		
		return new UIFormSheetBuilder(sheet);
	}
	
	
	protected UIFormComandBuilder addAction(String name, TextLocalizable caption) {
		return new UIFormComandBuilder(name, caption, this);
	}

	/**
	 * @param name
	 * @param caption 
	 * @param handler
	 */
	protected void addHandler(String name, TextLocalizable caption, UIActionHandler handler) {
		handlersByName.put(name, handler);
		
		SheetUICommandModel model = new SheetUICommandModel(){};
		
		model.setName(name);
		model.setText(caption);
		model.addUIActionHandler(handler);
		
		comands.add(model);
	}
	

	private class SheetUICommandModel extends AbstractUICommandModel {
		

		public void addUIActionHandler(UIActionHandler handler){
			super.addUIActionHandler(handler);
		}
	}
}
