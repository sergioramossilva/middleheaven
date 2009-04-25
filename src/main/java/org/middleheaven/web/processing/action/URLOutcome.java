package org.middleheaven.web.processing.action;

import java.util.Map;

import org.middleheaven.web.processing.Outcome;

public class URLOutcome extends Outcome {

	Map<String,String> params;
	public URLOutcome(String url, Map<String,String> params) {
		this(url,params,true);
	}
	
	public URLOutcome(String url, Map<String,String> params,boolean doRedirect) {
		super(BasicOutcomeStatus.SUCCESS, doRedirect, url);
		this.params = params;
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
