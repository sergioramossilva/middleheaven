package org.middleheaven.business.account;

import org.middleheaven.util.measure.Amount;
import org.middleheaven.util.measure.time.DateTimeHolder;

public abstract class AccountMovement<A extends Amount<A,?>> {

	private Account<A> acount;
	private DateTimeHolder timsestamp;
	private A amount;
	private AccountOperation operation;
	
	
	public Account<A> getAcount() {
		return acount;
	}
	public void setAcount(Account<A> acount) {
		this.acount = acount;
	}
	public DateTimeHolder getTimsestamp() {
		return timsestamp;
	}
	public void setTimsestamp(DateTimeHolder timsestamp) {
		this.timsestamp = timsestamp;
	}
	public A getAmount() {
		return amount;
	}
	public void setAmount(A amount) {
		this.amount = amount;
	}
	public AccountOperation getOperation() {
		return operation;
	}
	public void setOperation(AccountOperation operation) {
		this.operation = operation;
	}
	
	
}
