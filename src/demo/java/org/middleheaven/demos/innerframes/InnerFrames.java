package org.middleheaven.demos.innerframes;
import org.middleheaven.application.ApplicationLoadingService;
import org.middleheaven.core.bootstrap.client.DesktopStarter;
import org.middleheaven.core.wiring.Wire;


public class InnerFrames extends DesktopStarter {

	public static void main (String[] args){
		
		new InnerFrames().execute(args);
		
	}

	@Wire
	public void setAppService(ApplicationLoadingService cycleService) {
		
		cycleService.getApplicationContext().addModule(new InnerFramesApplication());
	}
}
