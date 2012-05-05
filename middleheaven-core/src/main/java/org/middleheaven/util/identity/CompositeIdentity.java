package org.middleheaven.util.identity;

import java.util.Arrays;

import org.middleheaven.util.Hash;

public class CompositeIdentity extends Identity {

	private final Identity[] identities;
	private final int hashCode;
	
	public static CompositeIdentity valueFor (Identity a, Identity b, Identity ... others ){
		Identity[] identities = new Identity[2 + others.length];
		
		identities[0] = a;
		identities[1] = b;
		
		System.arraycopy(others, 0, identities, 2, others.length);
		
		return new CompositeIdentity(identities);
	}
	
	private CompositeIdentity(Identity[] identities){
		this.identities = identities;
		this.hashCode = Hash.hash(identities).hashCode();
	}
	
	@Override
	public boolean equalsIdentity(Identity other) {
		return other instanceof CompositeIdentity && Arrays.equals(((CompositeIdentity)other).identities , this.identities);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return Arrays.toString(identities);
	}



}
