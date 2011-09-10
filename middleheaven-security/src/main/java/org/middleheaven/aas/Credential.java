package org.middleheaven.aas;

import java.io.Serializable;

/**
 * An autentication or autorization credential.
 * Is recomended that <code>Credentials</code>  implement equals and hashCode methods correctly.
 */
public interface Credential extends Serializable{

	public boolean equals(Object other);
	public int hashCode();
}
