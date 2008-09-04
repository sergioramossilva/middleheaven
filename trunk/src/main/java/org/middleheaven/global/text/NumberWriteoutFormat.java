package org.middleheaven.global.text;

import java.util.Locale;

import org.middleheaven.core.reflection.ReflectionUtils;

public abstract class NumberWriteoutFormat {

	public static Locale[] getAvailableLocales(){
		return new Locale[]{
				new Locale("pt","BR"),
				new Locale("pt","PT"),
				new Locale("es","ES"),
				new Locale("en","EN"),
				new Locale("fr","FR")
		};
	}
	
	public static NumberWriteoutFormat getInstance(Locale locale){
		String lang = locale.getLanguage();
		String country = locale.getCountry();
		if (country==null){
			country = lang.toUpperCase();
		}
		try{
			return (NumberWriteoutFormat) ReflectionUtils.newInstance("org.middleheaven.global.text." + country + lang + NumberWriteoutFormat.class.getSimpleName());
		} catch (Exception e){
			throw new FormatNotFoundException("Cannot find format class for locale " + locale);
		}
	}

	public String inWords(Number number){
		return format(number.toString());
	}

	protected final String format(String number){

		if (number.indexOf(".")>=0){
			String[] parts = number.toString().split("\\.");
			StringBuilder builder1 = new StringBuilder();
			builder1 = doPart(builder1,parts[0], false);
			StringBuilder builder2 = new StringBuilder();
			builder2 = doPart(builder2 , parts[1], true);

			return new StringBuilder()
			.append(builder1.toString().trim())
			.append(" ")
			.append(this.getInterGroupConcatenator(0,0,0))
			.append(" ")
			.append(builder2.toString().trim())
			.toString();

		} else {
			return doPart(new StringBuilder(), number, false).toString().trim();
		}
	}
	
	
	private StringBuilder doPart(StringBuilder builder, String part , boolean decimal){
		if (part.equals("0")){
			return builder.append(getGroupSufix(0,0,0)); 
		}
		int len = part.length();
		StringBuilder buffer = new StringBuilder();
		final int groupsCount = (int) Math.ceil(len/3d);
		int value =0;
		for (int group = 1 ; group <= groupsCount;group++){
			if (group>1){
				String s = this.getInterGroupConcatenator(value,group-1, group);
				if (!s.isEmpty()){
					builder.insert(0, " " + s);
				}
			}
			final String groupPart = part.substring(Math.max(0, len - group*3), len - (group-1)*3);
		    value = Integer.parseInt(groupPart);
			if (isNotable(value)){
				buffer.append(getWordsForNotable(value,group));
			} else {
				for (int indice = groupPart.length()-1; indice >=0  ; indice--){
					int ivalue = Integer.parseInt(groupPart.substring(groupPart.length() - indice - 1));
					if (ivalue<indice*10){
						continue;
					}
					buffer.append(getWordsFor(ivalue,group,indice));
					String s = getInnerGroupConcatenator(ivalue,group,indice);

					if (s!=null){
						buffer.append(' ');
						if (!s.isEmpty()){
							buffer.append(s).append(' ');
						} 
					} else {
						break;
					}

				}
			}
			
			if (decimal){
				buffer.append(" ").append(group==1 ? this.getFractionUnitName(len) : this.getGroupSufix(value,group,0));
			} else {
				buffer.append(" ").append(group==1 ? this.getUnitName() : this.getGroupSufix(value,group,0));
			}
			
			builder.insert(0, buffer).insert(0, " ");	
			buffer.delete(0, buffer.length());

		}


		return builder;
	}

	public abstract boolean isNotable(int value);
	public abstract String getGroupSufix(int value,int group,int indice);
	public abstract String getWordsForNotable(int value, int group);
	public abstract String getWordsFor(int value, int group, int indice);
	public abstract String getInnerGroupConcatenator(int value, int group, int indice);
	public abstract String getInterGroupConcatenator(int previousGroupValue, int previousGroup, int nextGroup);
	public abstract String getNegativeSufix();
	public abstract String getUnitName();
	public abstract String getFractionUnitName(int exponent);

}
