package org.middleheaven.aas;

import java.util.Set;

public class PermanentSignature extends TimedSignature {


	public PermanentSignature(Set<Credential>  credentials){
		super(credentials, null);
	}

	@Override
	public void refresh() {
		//no-op
	}

}
