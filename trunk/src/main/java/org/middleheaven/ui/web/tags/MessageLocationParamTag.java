package org.middleheaven.ui.web.tags;

public class MessageLocationParamTag extends AbstractTagSupport{


	public void setValue(Object obj){

		// sinaliza o formulário que se trata de um formulario de upload
		MessageLocalizationTag messageTag = this.findAncestorTag(MessageLocalizationTag.class);
		if (messageTag!=null){
			messageTag.addParam(obj);
		}
		
	}
}
