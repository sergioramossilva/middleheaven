package org.middleheaven.web.processing.action;

import java.util.Map;

public class URLOutcome extends Outcome {

	Map<String,String> params;
	public URLOutcome(String url, Map<String,String> params) {
		this(url,params,true);
	}
	
	public URLOutcome(String url, Map<String,String> params,boolean doRedirect) {
		super(OutcomeStatus.SUCCESS, doRedirect, url);
		this.params = params;
	}

	public String getParameterizedURL(){
		if (params.isEmpty()){
			return url;
		}
		
		StringBuilder param = new StringBuilder(url).append("?");
	
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
