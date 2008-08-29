package org.middleheaven.business.account;

import java.util.Collections;
import java.util.List;

import org.middleheaven.storage.DataStorage;
import org.middleheaven.util.Interval;
import org.middleheaven.util.measure.Amount;
import org.middleheaven.util.measure.time.DateHolder;

public abstract class AccountRepository<A extends Amount<A,?>> {

	private DataStorage ds;
	private A zero;
	
	public AccountRepository(A zero){
		this.zero = zero;
	}
	
	protected void setDataStorage (DataStorage ds){
		this.ds = ds;
	}
	
	public <C extends Account<A>> List<C> getOwnerAccounts(AccountOwner owner){
		return getOwnerAccounts(owner, null);
	}
	
	public A getAccountBalance ( Account<A> account, DateHolder date){
		List<AccountMovement<A>> movements = this.getAccountMovements(account, date, date);
		
		A total = zero;
		for ( AccountMovement<A> m : movements){
			total = total.plus(m.getAmount());
		}

		return total;
	}
	
	public final <M extends AccountMovement<A>> List<M> getAccountMovements( Account<A> account,Interval<? extends DateHolder> period){
		return getAccountMovements(account, period.start(),period.end());
	}
	
	private final <M extends AccountMovement<A>> List<M> getAccountMovements( Account<A> account,DateHolder inicialDate, DateHolder finalDate){
		return Collections.emptyList(); // TODO
	}
	
	public <C extends Account<A>> List<C> getOwnerAccounts(AccountOwner owner, AccountType type){
		return Collections.emptyList(); // TODO
	}
	
}
