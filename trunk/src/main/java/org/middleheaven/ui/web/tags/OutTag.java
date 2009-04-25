package org.middleheaven.ui.web.tags;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.TimepointFormatter.Format;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;

public class OutTag extends AbstractTagSupport {
	
	
	private Object value;
	
	
	public void setValue(Object value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		try {		
			
			if (value instanceof Date){
				pageContext.getOut().print(localize(TimeUtils.from((Date)value), Format.DATE_ONLY));
			} else if (value instanceof Calendar) {
				pageContext.getOut().print(localize(TimeUtils.from(((Calendar)value).getTime()), Format.DATE_ONLY));
			} else if (value instanceof CalendarDateTime) {
				pageContext.getOut().print(localize(((CalendarDateTime)value),Format.DATE_AND_TIME));
			} else if (double.class.isInstance(value)||Double.class.isInstance(value)) {
				pageContext.getOut().print(localize(new Double(value.toString())));
			
			} else if (value != null) {
				pageContext.getOut().print(value.toString());
			}
			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}
}