/**
 * 
 */
package org.middleheaven.domain.store.mapping;

/**
 * 
 */
public class DataSetColumnNotMatchPropertyMappingException extends
		DataSetMappingException {

	private static final long serialVersionUID = 8192135123128815665L;
	
	private String propertyName;
	private String typeName;

	/**
	 * Constructor.
	 * @param propertyName
	 * @param typeName
	 */
	public DataSetColumnNotMatchPropertyMappingException(
			String propertyName,
			String typeName) {
		super("Property in '" + typeName + "' mapping to data set column '" + propertyName + "' was not found." );
		
		this.propertyName = propertyName;
		this.typeName = typeName;
		
	}

	/**
	 * Obtains {@link String}.
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Obtains {@link String}.
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	
	

}
