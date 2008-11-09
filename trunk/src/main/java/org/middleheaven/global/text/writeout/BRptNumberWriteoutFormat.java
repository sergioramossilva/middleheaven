package org.middleheaven.global.text.writeout;

public class BRptNumberWriteoutFormat extends PTptNumberWriteoutFormat{

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



}
