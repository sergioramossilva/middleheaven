/**
 * 
 */
package org.middleheaven.domain;

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