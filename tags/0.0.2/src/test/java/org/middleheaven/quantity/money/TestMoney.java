package org.middleheaven.quantity.money;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;

public class TestMoney {

	@Test
	public void testMoney(){
		
		Money oneCent = Money.money("0.01", "USD");
		Real half = Real.fraction(1, 2);
		
		assertEquals(oneCent, oneCent.times(half));
	}
}


