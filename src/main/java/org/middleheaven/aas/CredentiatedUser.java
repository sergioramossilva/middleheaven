package org.middleheaven.aas;


public abstract class CredentiatedUser extends User {

	private static final long serialVersionUID = -1340053404100653870L;

	public void addPublicCredential(Credential credential){
		publicCredentials.add(credential);
	}
	
	public void removePublicCredential(Credential credential){
		publicCredentials.remove(credential);
	}
	

}
