/**
 * 
 */
package org.middleheaven.ui.models.form;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIComponent;

/**
 * 
 */
public class UIFormSheetBuilder {

	private UIFormSheetModel uiFormSheetModel;

	/**
	 * Constructor.
	 * @param name
	 * @param uiFormSheetModel
	 */
	public UIFormSheetBuilder(UIFormSheetModel uiFormSheetModel) {
		this.uiFormSheetModel = uiFormSheetModel;
	}

	/**
	 * @param string
	 * @param valueOf
	 * @return
	 */
	public UIFormSheetBuilder addField(Class<? extends UIComponent> type, String string, TextLocalizable valueOf) {


		return this;
	}

}
