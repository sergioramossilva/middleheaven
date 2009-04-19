package org.middleheaven.demos.shrinkgrow;
import org.middleheaven.application.ApplicationLoadingService;
import org.middleheaven.core.bootstrap.client.DesktopStarter;
import org.middleheaven.core.wiring.annotations.Wire;


public class TwoButtons extends DesktopStarter {

	public static void main (String[] args){
		
		new TwoButtons().execute(args);
		
	}

	@Wire
	public void setAppService(ApplicationLoadingService cycleService) {
		
		cycleService.getApplicationContext().addModule(new TwoButtonsApplication());
	}
}
