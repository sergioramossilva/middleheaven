package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.TimepointFormatter.Format;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;

public class OutTag extends AbstractTagSupport {
	
	
	private Object value;
	private int start = 0;
	private int end = -1;
	
	
	public void setValue(Object value) {
		this.value = value;
	}

	public void setStart(int value){
		this.start = value;
	}
	
	public void setEnd(int value){
		this.end  = value;
	}
	
	private void print(String str) throws IOException{
		if (start>=0 && end >0){
			str = str.substring(start, end);
		} else if (start > 0){
			str = str.substring(start);
		}
		
		pageContext.getOut().print(str);
	}
	
	public int doStartTag() throws JspException {
		try {		
			
			if (value instanceof Date){
				print(localize(TimeUtils.from((Date)value), Format.DATE_ONLY));
			} else if (value instanceof Calendar) {
				print(localize(TimeUtils.from(((Calendar)value).getTime()), Format.DATE_ONLY));
			} else if (value instanceof CalendarDateTime) {
				print(localize(((CalendarDateTime)value),Format.DATE_AND_TIME));
			} else if (double.class.isInstance(value)||Double.class.isInstance(value)) {
				print(localize(new Double(value.toString())));
			
			} else if (value != null) {
				print(value.toString());
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