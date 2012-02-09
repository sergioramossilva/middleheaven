/**
 * 
 */
package org.middleheaven.domain.model;


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