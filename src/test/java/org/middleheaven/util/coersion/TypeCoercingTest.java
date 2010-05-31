package org.middleheaven.util.coersion;

import org.junit.Test;

public class TypeCoercingTest {

	private enum A {
		
		AAA,
		BBB,
		CCC
	}
	
	@Test
	public void testEnumCoercing() {
		
		EnumNameTypeCoersor<A> c = new EnumNameTypeCoersor<A>();
		
		A a = c.coerceForward("AAA",A.class);
		
		
	}
}
