package org.middleheaven.business.account.warehouse;

import java.util.List;

import org.junit.Test;
import org.middleheaven.quantity.measurables.Mass;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.unit.SI;


public class TesteAccount {
	
	@Test
	public void testInterface(){
		
		Product p = null;
		ProductStockRepository rep = new ProductStockRepository(p);

		CalendarDate today = CalendarDate.today();
		
		List<ProductStock> stocks = rep.getOwnerAccounts(p);
		DecimalMeasure<Mass> total = DecimalMeasure.zero(SI.KILOGRAM);
		
		for (ProductStock s : stocks){
			DecimalMeasure<Mass> d = s.getBalance(today).cast();
			total = total.plus(d);
		}
		
		List<ProductMovement> movements = stocks.get(0).getMovements(today.upTo(today.nextDate().nextDate()));
		for (ProductMovement m : movements){
			m.getAcount().getOwner().equals(p);
		}
	}

}
