package org.middleheaven.domain;


public abstract class HardnameMapper {

	
	public abstract String getEntityHardname(Class<?> type);
	public abstract String getFieldHardname(Class<?> type , String logicFieldName);
	
	protected String mapEntityHardnameFromLogicname(String logicName){
		return logicName.toLowerCase();
	}
	
	protected String mapFieldHardnameFromLogicname(String logicName){
		return logicName.toLowerCase();
	}
}
