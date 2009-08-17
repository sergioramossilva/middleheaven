/*
 * Created on 2006/08/26
 *
 */
package org.middleheaven.global.text;

import java.io.Serializable;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.util.StringUtils;

/**
 * @author  Sergio M. M. Taborda 
 */
public class GlobalLabel implements Serializable{

	private String label;
	private String domain;
	private Object[] params = new Object[0];
	private LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
	
	public static GlobalLabel newInstance(String template){
		return new GlobalLabel (template);
	}

	public GlobalLabel(String domainlabel, Object[] params){
		if (domainlabel.indexOf(":")==0){
			throw new IllegalArgumentException("Label is not qualified in a domain");
		}
		String[] str = StringUtils.split(domainlabel, ":");
		this.label = str[1];
		this.domain = str[0];
		this.params = params;
	}

	public GlobalLabel(String domainlabel){
		if (domainlabel.indexOf(":")<0){
			throw new IllegalArgumentException("Label is not qualified in a domain");
		}
		String[] str = StringUtils.split(domainlabel, ":");
		this.label = str[1];
		this.domain = str[0];
	}

	public GlobalLabel(String domain, String label){
		this.label = label;
		this.domain = domain;
	}

	public GlobalLabel(String domain, String label , Object[] params){
		this.label = label;
		this.domain = domain;
		this.params = params;
	}


	public String getDomain(){
		return domain;
	}


	public String getLabel(){
		return label;
	}

	public String toString(){
		return domain + ":" + label;
	}

	public Object[] getMessageParams() {
		return this.params;
	}

	public String getLocalizedText(){
		return service.getMessage(this, false);

	}

	public String getLocalizedMnemonic(){
		return service.getMessage(this, true);

	}

	public boolean equals(Object other){
		return other instanceof  GlobalLabel && ((GlobalLabel)other).label.equals(label);
	}



}
