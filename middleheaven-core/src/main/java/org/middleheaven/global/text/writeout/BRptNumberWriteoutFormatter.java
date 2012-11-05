package org.middleheaven.global.text.writeout;

public class BRptNumberWriteoutFormatter extends PTptNumberWriteoutFormatter{

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "zero";
			case 2: return "mil";
			case 3: return value == 1 ? "milhão" : "milhões";
			case 4: return value == 1 ? "bilhão" : "bilhões";
			case 5: return value == 1 ? "trilhão" : "trilhões";
			case 6: return value == 1 ? "quadrilhão" : "quadrilhões";
			case 7: return value == 1 ? "quintilhão" : "quintilhões";
			case 8: return value == 1 ? "sextilhão"  : "sextilhões";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "dêcimos";
		case 2: return "centêsimos";
		case 3: return "milêsimos";
		case 4: return "dêcimos milêsimos";
		case 5: return "centêsimos milêsimos";
		case 6: return "milionêsimos";
		case 7: return "dêcimos milionêsimos";
		case 8: return "centêsimos milionêsimos";
		case 9: return "bilionêsimos";
		case 10: return "dêcimos bilionêsimos";
		case 11: return "centêsimos bilionêsimos";
		case 12: return "trilionêsimos";
		case 13: return "dêcimos trilionêsimos";
		case 14: return "centêsimos trilionêsimos";
		default : 
			throw new NumberOutOfRangeException();
		}
	}

}
