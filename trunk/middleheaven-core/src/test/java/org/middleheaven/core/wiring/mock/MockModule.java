package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;

//@Component
public class MockModule implements BindConfiguration {

	@Override
	public void configure(Binder binder) {
		binder.bind(Displayer.class).in(Shared.class).to(MockDisplay.class);
	}

}
