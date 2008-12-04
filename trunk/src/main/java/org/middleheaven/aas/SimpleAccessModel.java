package org.middleheaven.aas;


public class SimpleAccessModel implements AccessModel {

	private UserRolesModel userRolesModel;
	private RolesPermissionModel rolePermissionModel;

	public SimpleAccessModel(UserRolesModel userRolesModel,RolesPermissionModel rolePermissionModel) {
		this.userRolesModel = userRolesModel;
		this.rolePermissionModel = rolePermissionModel;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.aas.AccessModel#getUserRolesModel()
	 */
	public UserRolesModel getUserRolesModel(){
		return userRolesModel;
	}
	
	/* (non-Javadoc)
	 * @see org.middleheaven.aas.AccessModel#getRolePermissionModel()
	 */
	public RolesPermissionModel getRolePermissionModel(){
		return rolePermissionModel;
	}
}
