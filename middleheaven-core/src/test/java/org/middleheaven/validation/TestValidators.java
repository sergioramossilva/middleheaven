package org.middleheaven.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.util.validation.NotEmptyValidator;
import org.middleheaven.util.validation.SimpleEmailAddressValidator;
import org.middleheaven.util.validation.Validator;

public class TestValidators {

	
	@Test
	public void testNotEmptyValidator (){
		
		Validator<String> validator =  new NotEmptyValidator<String>();
		
		assertFalse(validator.validate("").isValid());
		assertFalse(validator.validate("   ").isValid());
		assertTrue(validator.validate("something").isValid());
		assertTrue(validator.validate("  something  ").isValid());
	}
	
	@Test
	public void testSimpleEmailValidator (){
		
		Validator<String> validator =  new SimpleEmailAddressValidator<String>();
		
		assertFalse(validator.validate("").isValid());
		assertFalse(validator.validate("   ").isValid());
		assertFalse(validator.validate("some.usersome.server.com").isValid());
		assertTrue(validator.validate("some.user@some.server.com").isValid());
		
	}
	
}
