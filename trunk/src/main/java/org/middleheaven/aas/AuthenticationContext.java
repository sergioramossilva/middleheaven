package org.middleheaven.aas;


public class AuthenticationContext {


	private static AuthenticationContext current = new AuthenticationContext(
			new EmptyUserRolesModel(),
			new EmptyRolePermissionModel()
	);

	public static AuthenticationContext getContext(){
		return current;
	}

	public static void setAuthenticationContext(AuthenticationContext context){
		current = context;
	}

	public static AuthenticationContext getAuthenticationContext(){
		return current;
	}

	private UserRolesModel userRolesModel;
	private RolesPermissionModel rolePermissionModel;

	public AuthenticationContext(UserRolesModel userRolesModel,RolesPermissionModel rolePermissionModel) {
		this.userRolesModel = userRolesModel;
	}

	public UserRolesModel getUserRolesModel(){
		return userRolesModel;
	}
	
	public RolesPermissionModel getRolePermissionModel(){
		return rolePermissionModel;
	}
}
