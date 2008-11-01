package org.middleheaven.test.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.conversion.TypeConvertions;


public class ConversionTest {

	@Test
	public void testIdentity() {
		String s = "test";
		assertEquals(s, TypeConvertions.convert(s, String.class));
		assertSame(s, TypeConvertions.convert(s, String.class));
		CharSequence cs = "test";
		assertEquals(cs, TypeConvertions.convert(s, String.class));
		assertSame(cs, TypeConvertions.convert(s, String.class));
		assertEquals(cs, TypeConvertions.convert(s, CharSequence.class));
		assertSame(cs, TypeConvertions.convert(s, CharSequence.class));
	}
	
	@Test
	public void testCharSequenceNumber() {
		
		String a = "1";
		StringBuilder ba = new StringBuilder("1");
		Long one = 1L;
		
		assertEquals(one, TypeConvertions.convert(a, Long.class));
		assertEquals(one, TypeConvertions.convert(ba, Long.class));
		assertEquals(a, TypeConvertions.convert(one, String.class));
		StringBuilder bs = TypeConvertions.convert(one, StringBuilder.class);
		assertEquals(ba.toString(), bs.toString());
		
		String b = "1.23";
		Double d = 1.23;
		
		assertEquals(d, TypeConvertions.convert(b, Double.class));
		assertEquals(b, TypeConvertions.convert(d, String.class));

	}
}
