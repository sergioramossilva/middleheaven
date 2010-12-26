/**
 * 
 */
package org.middleheaven.domain.model;

import org.middleheaven.domain.model.AbstractDataTypeModel;
import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.EditableEntityFieldModel;

class SimpleDataTypeModel extends AbstractDataTypeModel {
	
	private EditableEntityFieldModel model;

	SimpleDataTypeModel(EditableEntityFieldModel model){
		this.model = model;
	}
	
	@Override
	public DataType getDataType() {
		return model.getDataType();
	}
}