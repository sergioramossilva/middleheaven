package org.middleheaven.global.gov.br;

import org.middleheaven.global.gov.NDI;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationContext;
import org.middleheaven.validation.Validator;

public class BRCPFValidator implements Validator<NDI> {

	private boolean acceptAllEqual = false;
	
	@Override
	public void validate(ValidationContext context, NDI ndi) {
		final int[] combA = {10,9,8,7,6,5,4,3,2};
		final int[] combB = {11,10,9,8,7,6,5,4,3,2};
	    final int[] id = ndi.asIntArray();
		
	    if (id.length!=11){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }
	    
	    // first digit 
	    int dac = 0;
	    boolean equals=true;
	    int last = id[0];
	    for (int i=0;i<combA.length;i++){
	    	dac += id[i]*combA[i];
	    
	    	equals &= last==id[i];
	    	
	    	last=id[i];
	    }
	    
	    // digits cannot be all equal
	    if (!acceptAllEqual && equals){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }

	    
	    int vd = dac % 11;
	    vd = vd <=1 ? 0 : 11-vd;
	    
	    if( id[id.length-2] != vd){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }
	    
	    // Second Digit
	    dac = 0;
	    for (int i=0;i<combB.length;i++){
	    	dac += id[i]*combB[i];
	    }
	    
	    vd = dac % 11;
	    vd = vd <=1 ? 0 : 11-vd;
	    
	    if(id[id.length-1] != vd){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }
	}

	public boolean acceptAllEqual() {
		return acceptAllEqual;
	}

	public void setAcceptAllEqual(boolean acceptAllEqual) {
		this.acceptAllEqual = acceptAllEqual;
	}

}
