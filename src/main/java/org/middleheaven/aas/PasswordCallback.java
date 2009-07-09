package org.middleheaven.aas;


public class PasswordCallback implements Callback {

	
	private String prompt;
	private char[] password; 
	             
	public PasswordCallback(String prompt){
		this.prompt = prompt;
	}

	
	public char[] getPassword() {
		return password;
	}


	public void setPassword(char[] password) {
		this.password = password;
	}


	public String getPrompt() {
		return prompt;
	}


	@Override
	public boolean isBlank() {
		return password==null || password.length==0;
	}

}
