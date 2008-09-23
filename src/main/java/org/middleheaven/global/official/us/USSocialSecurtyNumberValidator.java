package org.middleheaven.global.official.us;

import org.middleheaven.global.official.NDI;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationContext;
import org.middleheaven.validation.Validator;

// http://en.wikipedia.org/wiki/Social_Security_Number#Valid_SSNs
public class USSocialSecurtyNumberValidator implements Validator<NDI> {

	private boolean acceptAd= false;
	
	@Override
	public void validate(ValidationContext context, NDI ndi) {
		
		if (ndi.length()!=9){
			context.add(MessageInvalidationReason.invalid());
		}else if (ndi.subGroup(0, 3).equals("000") || 
				ndi.subGroup(4, 2).equals("00") || 
				ndi.subGroup(6, 3).equals("000")){
			context.add(MessageInvalidationReason.invalid());
		} else if (ndi.subGroup(0, 8).equals("98765432")){
			// 987-65-4320 to 987-65-4329
			if (!acceptAd){
				context.add(MessageInvalidationReason.invalid());
			}
		} else if (ndi.subGroup(0, 3).asNumber()==null || ndi.subGroup(0, 3).asNumber().longValue()>772){
			context.add(MessageInvalidationReason.invalid());
		}
		
	}
	
	public void setAcceptAdvertisementReserved(boolean acceptAdvertisement){
		this.acceptAd = acceptAdvertisement;
	}
	
	public boolean acceptAdvertisementReserved(){
		return acceptAd;
	}

}
