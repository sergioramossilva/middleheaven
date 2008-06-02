package org.middleheaven.global.address;


import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class PostalCodeFormat extends Format {

	String mask;
	public PostalCodeFormat (String mask){
		this.mask = mask;
	}
	
	public String format (PostalCode code){
		return format(code, new StringBuffer(), new FieldPosition(0)).toString();
	}
	
	public PostalCode parse (String code){
		return (PostalCode)parseObject(code, new ParsePosition(0));
	}
	
	@Override
	public StringBuffer format(Object obj, StringBuffer buffer,FieldPosition pos) {
		PostalCode code = (PostalCode)obj;
		
		int i = -1;
		for (int m =0; m < mask.length();m++){
			switch (mask.charAt(m)){
			case '#':
				i++;

				if (i<code.length()){
					buffer.append(code.charAt(i));
				} else {
					buffer.append(" ");				
				}
				break;
			case '0':
				i++;
				if (i<code.length()){
					buffer.append(code.charAt(i));
				} else {
					buffer.append("0");				
				}
				break;
			default:
				buffer.append(mask.charAt(m));
			}
		}
		return buffer;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		StringBuilder builder = new StringBuilder();
		
		for (int i =0; i < source.length(); i++){
			char c = source.charAt(i);
			if (mask.indexOf(c)==-1){
				builder.append(c);
			}
		}
		
		return new PostalCode(builder.toString());

	}

}
