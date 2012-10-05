/**
 * 
 */
package org.middleheaven.ui.models;

import java.util.List;

import org.middleheaven.ui.UIActionHandler;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.models.form.UIFormSheetModel;

/**
 * 
 */
public interface UIFormModel extends UIModel {

	
	public List<UIFormSheetModel> getFormSheets();
	
	/**
	 * Obtains the {@link UIActionHandler} for the given commandName.
	 * @param commandName
	 * @return
	 */
	public UIActionHandler getUIActionHandler(String commandName);
	
	public List<UICommandModel> getActions();
}
