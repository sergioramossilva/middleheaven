package org.middleheaven.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.coersion.TypeCoercing;


public class ConversionTest {

	@Test
	public void testIdentity() {
		String s = "test";
		assertEquals(s, TypeCoercing.convert(s, String.class));
		assertSame(s, TypeCoercing.convert(s, String.class));
		CharSequence cs = "test";
		assertEquals(cs, TypeCoercing.convert(s, String.class));
		assertSame(cs, TypeCoercing.convert(s, String.class));
		assertEquals(cs, TypeCoercing.convert(s, CharSequence.class));
		assertSame(cs, TypeCoercing.convert(s, CharSequence.class));
	}
	
	@Test
	public void testCharSequenceNumber() {
		
		String a = "1";
		StringBuilder ba = new StringBuilder("1");
		Long one = 1L;
		
		assertEquals(one, TypeCoercing.convert(a, Long.class));
		assertEquals(one, TypeCoercing.convert(ba, Long.class));
		assertEquals(a, TypeCoercing.convert(one, String.class));
		StringBuilder bs = TypeCoercing.convert(one, StringBuilder.class);
		assertEquals(ba.toString(), bs.toString());
		
		String b = "1.23";
		Double d = 1.23;
		
		assertEquals(d, TypeCoercing.convert(b, Double.class));
		assertEquals(b, TypeCoercing.convert(d, String.class));

	}
}
