package org.middleheaven.ui;

public enum UIReadState {
	
	INVISIBLE,
	OUTPUT_ONLY,
	INPUT_DISABLED,
	INPUT_ENABLED;
	
	
	public boolean isVisible(){
		return this!= INVISIBLE;
	}
	
	public boolean isEnabled(){
		return this == INPUT_ENABLED;
	}
	
	public boolean isEditable(){
		return this == INPUT_ENABLED || this == INPUT_DISABLED;
	}
}
