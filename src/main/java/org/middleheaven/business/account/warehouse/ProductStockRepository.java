package org.middleheaven.business.account.warehouse;

import org.middleheaven.business.account.AccountRepository;
import org.middleheaven.util.measure.DecimalMeasure;

public class ProductStockRepository extends AccountRepository<DecimalMeasure<?>> {

	public ProductStockRepository(Product p) {
		super(DecimalMeasure.zero(p.getUnit()));
	}

}
