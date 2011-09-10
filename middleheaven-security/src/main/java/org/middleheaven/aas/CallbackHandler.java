package org.middleheaven.aas;


public interface CallbackHandler {

	CallbacksSet getCallbacks();

	void onException(AccessException e);


}
