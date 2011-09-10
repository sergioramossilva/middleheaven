package org.middleheaven.aas;

public enum SuccessLevel {

	/**
	 * The {@code AutenticationModule} is required to succeed.
	 * If it succeeds or fails, authentication still continues
	 * to proceed down the {@code AutenticationModule} list.
	 */
	REQUIRED,
	
	/**
	 * The {@code AutenticationModule} is required to succeed.
	 *  If it succeeds, authentication continues down the {@code AutenticationModule} list.  
	 *  If it fails,control immediately returns to the application 
	 *  (authentication does not proceed down the {@code AutenticationModule} list).
	 */
	REQUISITE,
	
	/**
	 * The {@code AutenticationModule} is <i>not</i> required to succeed.  
	 *  If it does succeed, control immediately returns to the application 
	 *  (authentication does not proceed down the {@code AutenticationModule} list).
	 *  If it fails, authentication continues down the {@code AutenticationModule} list.
	 */
	SUFFICIENT ,
	
	/**
	 * The {@code AutenticationModule} is not required to succeed. 
	 * If it succeeds or fails,authentication still
	 * continues to proceed down the {@code AutenticationModule} list.
	 */
	OPTIONAL;
}
