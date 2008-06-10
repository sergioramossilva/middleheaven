package org.middleheaven.injection.mock;

import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;

public class MockModule implements BindConfiguration {

	@Override
	public void configure(Binder binder) {
		binder.bind(Displayer.class).to(MockDisplay.class);
	}

}
