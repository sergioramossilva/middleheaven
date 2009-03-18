package org.middleheaven.global.gov.br;

import org.middleheaven.global.gov.NDI;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationContext;
import org.middleheaven.validation.Validator;

public final class BRCNPJValidator implements Validator<NDI> {

	private static final int MODULE = 11;
	private static final int CNPJ_LENGTH = 14;
	private boolean acceptAllEqual = false;
	
	public BRCNPJValidator(){}
	
	@Override
	public void validate(ValidationContext context, NDI ndi) {
		final int[] combA = {5,4,3,2,9,8,7,6,5,4,3,2};
		final int[] combB = {6,5,4,3,2,9,8,7,6,5,4,3,2};
	    final int[] id = ndi.asIntArray();
		
	    if (id.length != CNPJ_LENGTH){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }
	    
	    // first digit 
	    int dac = 0;
	    for (int i=0;i<combA.length;i++){
	    	dac += id[i]*combA[i];
	    }
	    
	    int vd = dac % MODULE;
	    vd = vd <=1 ? 0 : MODULE - vd;
	    
	    if( id[id.length - 2] != vd){
	    	context.add(MessageInvalidationReason.invalid());
	    	return;
	    }
	    
	    // second digit
	    dac = 0;
	    for (int i=0;i<combB.length;i++){
	    	dac += id[i]*combB[i];
	    }
	    
	    vd = dac % MODULE;
	    vd = vd <= 1 ? 0 : MODULE - vd;
	    
	    if (id[id.length - 1] != vd) {
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
