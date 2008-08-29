package org.middleheaven.business.account;

public enum AccountOperation {

	CREDIT,
	DEBIT;
	
	public AccountOperation invert (){
		return this==CREDIT ? DEBIT : CREDIT;
	}
	
}
