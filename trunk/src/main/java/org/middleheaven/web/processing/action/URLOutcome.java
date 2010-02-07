package org.middleheaven.web.processing.action;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.Outcome;

public class URLOutcome extends Outcome {

	public static URLOutcome forUrl(String url){
		return new URLOutcome(url);
	}
	
	private Map<String,String> params = new HashMap<String,String>();

	private URLOutcome(String url) {
		super(BasicOutcomeStatus.SUCCESS, url, false , HttpCode.OK);
	}

	public URLOutcome byRedirecting(){
		this.setRedirect(true);
		return this;
	}
	
	public URLOutcome withParams(Map<String,String> params){
		this.params.putAll(params);
		return this;
	}
	
	public URLOutcome withParam(String name, String value){
		this.params.put(name, value);
		return this;
	}
	
	public URLOutcome withCode(HttpCode code){
		this.setHttpCode(code);
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
