package org.middleheaven.web;

import org.middleheaven.ui.Context;


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
		public void interceptForward(Context context) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interceptReverse(Context context) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
