/**
 * 
 */
package org.middleheaven.domain.model;

import org.middleheaven.domain.model.AbstractDataTypeModel;
import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.BeanEditableEntityFieldModel;

class SimpleDataTypeModel extends AbstractDataTypeModel {
	
	private BeanEditableEntityFieldModel model;

	SimpleDataTypeModel(BeanEditableEntityFieldModel model){
		this.model = model;
	}
	
	@Override
	public DataType getDataType() {
		return model.getDataType();
	}
}