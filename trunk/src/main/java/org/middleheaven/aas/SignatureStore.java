package org.middleheaven.aas;

import org.middleheaven.ui.AttributeContext;



public interface  SignatureStore<C extends AttributeContext> {

	public Signature getSignature(C context);

	public void setSignature(C context, Signature signature);
}
