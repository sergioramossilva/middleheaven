package org.middleheaven.ui.web.tags;

import java.util.Date;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.ui.ContextScope;

public class AbstractTagSupport extends TagSupport {


	public String localize(CalendarDateTime date ,TimepointFormatter.Format format){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getTimestampFormatter(culture).format(date,format);
	}
	
	public String localize(GlobalLabel message,ContextScope scope){
		
		LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
		return service.getMessage(new TagContext(pageContext).getCulture(), message, false);
		
	}
	
	public String localize(Number number){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getQuantityFormatter(culture).format(Real.valueOf(number));
	}
	
	public <T extends Tag> T findAncestorTag(Class<T> type){
		return type.cast(this.findAncestorWithClass(this, type));
	}
	
	public void writeAttribute(CharSequence name, Object value) throws JspTagException{
		try {
			pageContext.getOut().append(" ").append(name)
			.append("=\"").append(value==null ? "" :  value.toString()).append("\" ");
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
