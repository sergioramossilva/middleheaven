package org.middleheaven.test.ui.web;

import org.middleheaven.web.Interceptor;
import org.middleheaven.web.InterceptorChain;
import org.middleheaven.web.OutcomeStatus;
import org.middleheaven.web.PresenterCommandMappingBuilder;
import org.middleheaven.web.WebContext;



public class TesteWriting {

	
	public void testWriting(){
		
		PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.with(new ProjectInterceptor())
		.withAction("save")
		.on(OutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(OutcomeStatus.FAILURE).forwardTo("genericfailure.html");
		
		PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.with(new ProjectInterceptor())
		.withAction("save")
		.on(OutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(OutcomeStatus.FAILURE).redirectTo(404);

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
