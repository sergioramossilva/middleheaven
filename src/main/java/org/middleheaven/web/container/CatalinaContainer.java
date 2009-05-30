/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.work.scheduled.AlarmClockScheduleWorkExecutionServiceActivator;

public class CatalinaContainer extends WebContainer  {


    public CatalinaContainer(ServletContext context){
        super(context);
    }
    
	@Override
	public void init(WiringService wiringService) {
		SetActivatorScanner scanner = new SetActivatorScanner();
		scanner.addActivator(AlarmClockScheduleWorkExecutionServiceActivator.class);
		wiringService.addActivatorScanner(scanner);
	}

    @Override
    public String getEnvironmentName() {
        // TODO pegar detalhes, versão OS ,etc..
        return "Tomcat";
    }






}
