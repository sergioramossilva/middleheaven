package org.middleheaven.global.text.writeout;

/**
 * 
 * @see http://www.grammarbook.com/numbers/numbers.asp
 */
class GBenNumberWriteoutFormat extends NumberWriteoutFormatter{

	public GBenNumberWriteoutFormat(){}
	
	@Override
	public String getNegativeSufix() {
		return "negative";
	}

	@Override
	public String getUnitName() {
		return "";
	}

	@Override
	public boolean isNotable(int value) {
		return value < 19;
	}

	@Override
	public String getWordsForNotable(int value, int group) {
		switch (value){
		case 1: return "one";
		case 2: return "two";
		case 3: return "three";
		case 4: return "four";
		case 5: return "five";
		case 6: return "six";
		case 7: return "seven";
		case 8: return "eight";
		case 9: return "nine";
		case 10: return "ten";
		case 11: return "eleven";
		case 12: return "twelve";
		case 13: return "thirteen";
		case 14: return "fourteen";
		case 15: return "fifteen";
		case 16: return "sixteen";
		case 17: return "seventeen";
		case 18: return "eighteen";
		case 19: return "nineteen";
		default : return "";
		}
	}

	@Override
	public String getWordsFor(int value, int group, int indice) {
		
		if (indice == 0){
			return getWordsForNotable(value,group);
		} else if (indice == 1){
			int rvalue = value%100;
			if (rvalue<20){
				return getWordsForNotable(rvalue,group);
			} else {
				return getDozen(rvalue) + (rvalue%10==0 ? "" : "-" + getWordsForNotable(rvalue % 10,group));
			}
		} else if (indice == 2){
			return getWordsFor(value/100, 1 , 0) + " hundred";
		} else {
			return "";
		}
	}

	private String getDozen(int value){
		switch (value/10){
		case 2: return "twenty";
		case 3: return "thirty";
		case 4: return "forty";
		case 5: return "fifty";
		case 6: return "sixty";
		case 7: return "seventy";
		case 8: return "eighty";
		case 9: return "ninety";
		default : return "";
		}
	}
	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "tenth";
		case 2: return "hundredth";
		case 3: return "thousandth";
		case 4: return "millionth";
		case 5: return "billionth";
		case 6: return "trillionth";
		default : throw new NumberOutOfRangeException();
		}
	}

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "zero";
			case 2: return "thousand";
			case 3: return "million" ;
			case 4: return "bilion" ;
			case 5: return "trilion" ;
			default : 
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getInnerGroupConcatenator(int value,int group, int indice) {
		return value > 100 ? "and" : null;
	}

	@Override
	public String getInterGroupConcatenator(int previousGroupValue,int previousGroup, int nextGroup) {
		return previousGroupValue <100 ? "and" : ",";
	}











}
