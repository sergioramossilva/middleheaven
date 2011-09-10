package org.middleheaven.ui;

public enum UIReadState {
	
	INVISIBLE,
	OUTPUT_ONLY,
	INPUT_DISABLED,
	INPUT_ENABLED;
	
	
	public static UIReadState computeFrom(boolean visible , boolean enabled, boolean readOnly){
		if (visible){
			if (readOnly){
				return OUTPUT_ONLY;
			} else if (enabled) {
				return INPUT_ENABLED;
			} else {
				return INPUT_DISABLED;
			}
		} else {
			return INVISIBLE;
		}
	}
	
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
