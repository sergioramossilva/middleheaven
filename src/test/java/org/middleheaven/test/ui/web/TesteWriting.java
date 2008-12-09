package org.middleheaven.test.ui.web;

import org.middleheaven.web.Interceptor;
import org.middleheaven.web.InterceptorChain;
import org.middleheaven.web.OutcomeStatus;
import org.middleheaven.web.PresenterCommandMappingBuilder;
import org.middleheaven.web.WebCommandMapping;
import org.middleheaven.web.WebContext;



public class TesteWriting {

	
	public void testWriting(){
		
		WebCommandMapping wm = PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.to("/ref/projeto*")
		.with(new ProjectInterceptor())
		.on(OutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(OutcomeStatus.FAILURE).forwardTo("genericfailure.html")
		.build();
		
		WebCommandMapping wm2 = PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.with(new ProjectInterceptor())
		.on(OutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(OutcomeStatus.FAILURE).redirectTo(404)
		.build();
	}
	
	
	private static class ProjectPresenter{
		
	}
	
	private static class ProjectInterceptor implements Interceptor{

		@Override
		public void intercept(WebContext context, InterceptorChain chain) {
			// TODO implement ProjectInterceptor.intercept
			
		}
		
	}
}
