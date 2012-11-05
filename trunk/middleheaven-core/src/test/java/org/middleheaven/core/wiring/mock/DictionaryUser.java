/**
 * 
 */
package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Component;



/**
 * 
 */
@Component
public class DictionaryUser {

	
	private DictionaryService service;

	public DictionaryUser (@GermanDicionary DictionaryService service){
		this.service = service;
	}
	
	public String getLang(){
		return service.getLang();
	}
}
