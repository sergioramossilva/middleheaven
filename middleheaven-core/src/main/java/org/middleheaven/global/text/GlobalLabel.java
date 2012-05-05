/*
 * Created on 2006/08/26
 *
 */
package org.middleheaven.global.text;

import java.io.Serializable;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.util.Hash;
import org.middleheaven.util.StringUtils;

/**
 * 
 */
public class GlobalLabel implements Serializable{

	private static final long serialVersionUID = 3624294714337027974L;
	
	private String label;
	private String domain;
	private Object[] params = new Object[0];
	private LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
	
	/**
	 * 
	 * @param template
	 * @return
	 */
	public static GlobalLabel of(String template){
		return new GlobalLabel (template);
	}
	
	public static GlobalLabel of(String domain, String label){
		return new GlobalLabel (domain, label);
	}

	public GlobalLabel(String domainlabel, Object[] params){
		if (domainlabel.indexOf(":")<0){
			throw new IllegalArgumentException("Label " + domainlabel + " is not qualified in a domain");
		}
		setLabelAndDomainFrom(domainlabel);
		this.params = params;
	}

	public GlobalLabel(String domain, String label){
		this.label = label;
		this.domain = domain;
	}
	
	public GlobalLabel(String domainlabel){
		if (domainlabel.indexOf(":")<0){
			throw new IllegalArgumentException("Label " + domainlabel + " is not qualified in a domain");
		}
		setLabelAndDomainFrom(domainlabel);
	}

	private void setLabelAndDomainFrom(String domainLabel){
		String[] str = StringUtils.split(domainLabel, ":");
		this.label = str.length > 1 ? str[1] : "";
		this.domain = str[0];
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

	public boolean equals(Object other){
		return other instanceof  GlobalLabel && ((GlobalLabel)other).label.equals(label);
	}

    public int hashCode(){
    	return Hash.hash(label).hashCode();
    }

}
