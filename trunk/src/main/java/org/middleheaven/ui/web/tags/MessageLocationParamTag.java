package org.middleheaven.ui.web.tags;

public class MessageLocationParamTag extends AbstractTagSupport{


	public void setValue(Object obj){

		MessageLocalizationTag messageTag = this.findAncestorTag(MessageLocalizationTag.class);
		if (messageTag!=null){
			messageTag.addParam(obj);
		}
		
	}
}
