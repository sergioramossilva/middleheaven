package org.middleheaven.aas.old;

import org.middleheaven.aas.Signature;
import org.middleheaven.aas.Subject;

public interface SignaturePolicy {

	Signature createSignature(Subject acessor);

}
