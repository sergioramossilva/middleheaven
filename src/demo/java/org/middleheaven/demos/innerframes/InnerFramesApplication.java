package org.middleheaven.demos.innerframes;

import java.io.File;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.application.MainApplicationModule;
import org.middleheaven.core.wiring.Wire;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.XMLUIComponentBuilder;
import org.middleheaven.util.Version;

public class InnerFramesApplication extends MainApplicationModule{

	private UIService service;

	public InnerFramesApplication() {
		super(new ApplicationID("innerframes", Version.from(0, 0, 0)));
	
	}

	@Wire
	public void setUIService(UIService service){
		this.service = service;
	}
	
	@Override
	public void load(ApplicationContext context) {
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIEnvironment root = xmlBuilder.buildFrom(new File("./src/demo/java/org/middleheaven/demos/innerframes/ui.xml"));
		
		service.registerEnvironment(root, true);
	}

	@Override
	public void unload(ApplicationContext context) {
		// no-op
	}

}