package org.middleheaven.web.processing.action;


public final class RedirectOptions {

	public static RedirectOptions temporary(){
		return new RedirectOptions(false);
	}
	
	public static RedirectOptions permanent(){
		return new RedirectOptions(true);
	}

	private boolean permanent = false;

	private RedirectOptions(boolean permanent){
		this.permanent = permanent;
	}
	
	
	public boolean isPermanent(){
		return this.permanent;
	}

}

