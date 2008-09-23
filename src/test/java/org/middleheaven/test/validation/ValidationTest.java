package org.middleheaven.test.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.global.official.NDI;
import org.middleheaven.global.official.br.BRCNPJValidator;
import org.middleheaven.global.official.br.BRCPFValidator;
import org.middleheaven.global.official.us.USSocialSecurtyNumberValidator;
import org.middleheaven.validation.DefaultValidationContext;
import org.middleheaven.validation.LogicValidator;


public class ValidationTest {

	@Test
	public void testBRGovCPFValidate(){
		DefaultValidationContext context = new DefaultValidationContext();
		BRCPFValidator validator = new BRCPFValidator();
		NDI ndi;
		
		context.clear();
		validator.setAcceptAllEqual(true);
		ndi = new NDI("11111111111");
		context.apply(validator, ndi);
		assertTrue(context.isValid());
		
	
		validator.setAcceptAllEqual(false);
			
		context.clear();
	    ndi = new NDI("12345678909");
		context.apply(validator, ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("55847609604");
		context.apply(validator, ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("12345678919");
		context.apply(validator, ndi);
		assertFalse(context.isValid());
		
		context.clear();
		ndi = new NDI("11111111111");
		context.apply(validator, ndi);
		assertFalse(context.isValid());
	}
	
	@Test
	public void testBRGovCNPJValidate(){
		
	
		DefaultValidationContext context = new DefaultValidationContext();
		NDI ndi;
		
		BRCNPJValidator validator = new BRCNPJValidator();

		context.clear();
		ndi = new NDI("11222333000181");
		context.apply(validator, ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(validator, ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("11222333000196");
		context.apply(validator, ndi);
		assertFalse(context.isValid());

	}
	
	@Test
	public void testLogicValidatorValidate(){
		DefaultValidationContext context = new DefaultValidationContext();
		NDI ndi;
			
		BRCNPJValidator a = new BRCNPJValidator();
		BRCPFValidator b = new BRCPFValidator();
		
		
		// use a CNPJ
		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(LogicValidator.and(a, b), ndi);
		assertFalse(context.isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(LogicValidator.and(b, a), ndi);
		assertFalse(context.isValid());

		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(LogicValidator.or(a, b), ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(LogicValidator.or(b, a), ndi);
		assertTrue(context.isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		context.apply(LogicValidator.or(b, b), ndi);
		assertFalse(context.isValid());
	}
	
	@Test
	public void testUSGovValidate(){
		
		DefaultValidationContext context = new DefaultValidationContext();
		USSocialSecurtyNumberValidator validator = new USSocialSecurtyNumberValidator();
		
		NDI ndi = new NDI("123456789");
		
		context.apply(validator, ndi);
		
		assertTrue(context.isValid());
		
		ndi = new NDI("987654328");
		
		context.apply(validator, ndi);
		
		assertFalse(context.isValid());

		context.clear();
		ndi = new NDI("77390890");
		
		context.apply(validator, ndi);
		
		assertFalse(context.isValid());
		
		context.clear();
		ndi = new NDI("00089765");
		
		context.apply(validator, ndi);
		
		assertFalse(context.isValid());
		
		context.clear();
		ndi = new NDI("12300765");
		
		context.apply(validator, ndi);
		
		assertFalse(context.isValid());
		
		context.clear();
		ndi = new NDI("12367000");
		
		context.apply(validator, ndi);
		
		assertFalse(context.isValid());
		
		// accept advertisement
		ndi = new NDI("987654328");
		
		context.clear();
		validator.setAcceptAdvertisementReserved(true);
		context.apply(validator, ndi);
		
		assertTrue(context.isValid());
	}
}
