package org.middleheaven.web.processing.action;

import java.util.Map;

import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.Outcome;

public class URLOutcome extends Outcome {

	public static URLOutcome forUrl(String url){
		return new URLOutcome(url);
	}
	
	private Map<String,String> params;

	private URLOutcome(String url) {
		super(BasicOutcomeStatus.SUCCESS, url, false , HttpCode.OK);
	}

	public URLOutcome byRedirecting(){
		this.setRedirect(true);
		return this;
	}
	
	public URLOutcome withParams(Map<String,String> params){
		this.params = params;
		return this;
	}
	
	public String getParameterizedURL(){
		if (params.isEmpty()){
			return this.getUrl();
		}
		
		StringBuilder param = new StringBuilder(getUrl()).append("?");
	
	    for (Map.Entry<String,String> parameter : params.entrySet()){
	        
	        param.append(parameter.getKey())
	        .append("=")
	        .append(parameter.getValue())
	        .append("&");
	        
	    }
	    
	    param.delete(param.length()-1, param.length());
	    
	    return param.toString();
	}
}
