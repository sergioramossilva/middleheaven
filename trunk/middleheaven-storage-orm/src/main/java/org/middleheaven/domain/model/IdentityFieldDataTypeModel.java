/**
 * 
 */
package org.middleheaven.domain.model;

/**
 * 
 */
public class IdentityFieldDataTypeModel implements DataTypeModel {

	
	private DataType dataType;
	private Class<?> identityType;
	
	public IdentityFieldDataTypeModel (DataType dataType){
		this.dataType = dataType;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataType getDataType() {
		return dataType;
	}
	/**
	 * @param type
	 */
	public void setIdentityType(Class<?> type) {
		this.identityType = type;
	}
	/**
	 * Obtains {@link Class<?>}.
	 * @return the identityType
	 */
	public Class<?> getIdentityType() {
		return identityType;
	}

	
}
