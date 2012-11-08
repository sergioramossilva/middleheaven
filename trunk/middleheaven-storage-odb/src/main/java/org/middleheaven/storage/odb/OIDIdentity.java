package org.middleheaven.storage.odb;

import org.middleheaven.util.identity.Identity;
import org.neodatis.odb.OID;

/**
 * 
 */
public final class OIDIdentity extends Identity {


	private static final long serialVersionUID = -4243489196062058690L;
	
	private OID oid;

	public static OIDIdentity valueOf(OID objectId) {
		return objectId ==null ? null : new OIDIdentity(objectId);
	}
	
	private OIDIdentity(OID oid){
		this.oid = oid;
	}
	
	@Override
	public boolean equalsIdentity(Identity other) {
		return other instanceof OIDIdentity && ((OIDIdentity)other).oid.equals(this.oid);
	}

	@Override
	public int hashCode() {
		return oid.hashCode();
	}

	@Override
	public String toString() {
		return oid.toString();
	}


}
