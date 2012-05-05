package org.middleheaven.global.atlas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.global.address.PostalCode;
import org.middleheaven.global.address.PostalCodeFormat;


public class PostalCodeTest {

	@Test
	public void testPostalCodeFormat (){

		PostalCodeFormat format= new PostalCodeFormat("#####-000");
		PostalCode code = new PostalCode("12345678");

		assertEquals("12345-678" , format.format(code));
		
		PostalCode ncode = format.parse("12345-678");
		
		assertEquals(code , ncode);
		
		assertEquals("12345-670" , format.format(new PostalCode("1234567")));
		
	    format= new PostalCodeFormat("#####-000");
		
		assertEquals("1234 -000" , format.format(new PostalCode("1234")));
		
	}
}
