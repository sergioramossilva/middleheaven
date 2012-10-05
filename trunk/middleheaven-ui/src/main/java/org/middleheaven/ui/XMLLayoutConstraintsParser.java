/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.ui.models.UILayoutModel;
import org.middleheaven.ui.models.impl.UIBorderLayoutModel;

/**
 * 
 */
public class XMLLayoutConstraintsParser {

	
	public static UILayoutConstraint parseConstraint(String layoutConstraint, UILayoutModel layoutModel) {
		
		
		if (layoutModel instanceof UIBorderLayoutModel) {
			return UIBorderLayoutModel.UIBorderLayoutConstraint.valueOf(layoutConstraint.toUpperCase());
		} else {
			throw new IllegalArgumentException("'" + layoutConstraint + "' is not a valid constraint for model " + layoutModel.getClass());
		}
	}
}
