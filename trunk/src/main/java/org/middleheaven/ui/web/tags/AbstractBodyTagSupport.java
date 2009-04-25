package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.ui.ContextScope;


public class AbstractBodyTagSupport extends BodyTagSupport {

	public <T extends Tag> T findAncestorTag(Class<T> type){
		return type.cast(this.findAncestorWithClass(this, type));
	}
	
	public String localize(GlobalLabel message){
		return localize(message,ContextScope.APPLICATION);
	}
	
	public String localize(GlobalLabel message,ContextScope scope){
		
		Culture culture = new TagContext(pageContext).getCulture();
		
		LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
		return service.getMessage(culture, message, false);
		
	}
	
	public void writeAttribute(CharSequence name, Object value) throws JspTagException{
		if (value==null){
			return;
		}
		try {
			pageContext.getOut().append(" ").append(name)
			.append("=\"").append(value.toString()).append("\"");
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}
	
	public void write(CharSequence text) throws JspTagException{
		try {
			pageContext.getOut().print(text);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}
	
	public void writeLine(CharSequence text) throws JspTagException{
		try {
			pageContext.getOut().println(text);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	
	
}
