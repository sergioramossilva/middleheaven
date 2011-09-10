package org.middleheaven.business.account.warehouse;

import org.middleheaven.business.account.AccountRepository;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.DecimalMeasure;

public class ProductStockRepository<E extends Measurable> extends AccountRepository<DecimalMeasure<E>> {

	
	
	public ProductStockRepository(Product p) {
		super ((DecimalMeasure<E>) DecimalMeasure.zero(p.getUnit()));
	}

}
