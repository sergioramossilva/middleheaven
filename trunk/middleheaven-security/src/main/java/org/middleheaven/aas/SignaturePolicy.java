package org.middleheaven.aas;

import java.util.Set;


public interface SignaturePolicy {

	Signature createSignature(Set<Credential> credentials);

}
