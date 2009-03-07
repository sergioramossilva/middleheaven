package org.middleheaven.aas;

import org.middleheaven.quantity.time.DateTimeHolder;

public interface ExpirableCredential extends Credential {

	/**
	 * Determines if the credencial has experied
	 * @param now
	 * @return
	 */
	public boolean hasExpired(DateTimeHolder now);
}
