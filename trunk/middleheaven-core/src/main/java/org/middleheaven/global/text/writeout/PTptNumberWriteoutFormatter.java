package org.middleheaven.global.text.writeout;

class PTptNumberWriteoutFormatter extends NumberWriteoutFormatter{

	public PTptNumberWriteoutFormatter(){}
	
	@Override
	public String getNegativeSufix() {
		return "negativo";
	}

	@Override
	public String getUnitName() {
		return "";
	}

	@Override
	public boolean isNotable(int value) {
		return (value==100 || value < 20);
	}

	@Override
	public String getWordsForNotable(int value, int group) {
		switch (value){
		case 1: return "um";
		case 2: return "dois";
		case 3: return "três";
		case 4: return "quatro";
		case 5: return "cinco";
		case 6: return "seis";
		case 7: return "sete";
		case 8: return "oito";
		case 9: return "nove";
		case 10: return "dez";
		case 11: return "onze";
		case 12: return "doze";
		case 13: return "treze";
		case 14: return "quatorze";
		case 15: return "quinze";
		case 16: return "dezaseis";
		case 17: return "dezasete";
		case 18: return "dezoito";
		case 19: return "dezanove";
		case 100: return "cem";
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
				switch (rvalue/10){
				case 2: return "vinte";
				case 3: return "trinta";
				case 4: return "quarenta";
				case 5: return "cinquenta";
				case 6: return "sessenta";
				case 7: return "setenta";
				case 8: return "oitenta";
				case 9: return "noventa";
				default : return "";
				}
			}
		} else if (indice == 2){
			switch (value/100){
			case 1: return "cento";
			case 2: return "duzentos";
			case 3: return "trezentos";
			case 4: return "quatrocentos";
			case 5: return "quinhentos";
			case 6: return "seissentos";
			case 7: return "setesentos";
			case 8: return "oitocentos";
			case 9: return "novecentos";
			default : return "";
			}
		} else {
			return "";
		}
	}

	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "décimos";
		case 2: return "centésimos";
		case 3: return "milésimos";
		case 4: return "décimos milésimos";
		case 5: return "centésimos milésimos";
		case 6: return "milionésimos";
		case 7: return "décimos milionésimos";
		case 8: return "centésimos milionésimos";
		case 9: return "bilionésimos";
		case 10: return "décimos bilionésimos";
		case 11: return "centésimos bilionésimos";
		case 12: return "trilionésimos";
		case 13: return "décimos trilionésimos";
		case 14: return "centésimos trilionésimos";
		default : 
			throw new NumberOutOfRangeException();
		}
	}

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "zero";
			case 2: return "mil";
			case 3: return value == 1 ? "milhão" : "milhões";
			case 4: return value == 1 ? "milhar de milhão" : "milhares de milhão";
			case 5: return value == 1 ? "bilhão" : "bilh�es";
			case 6: return value == 1 ? "milhar de bilhão" : "milhares de bilhão";
			case 7: return value == 1 ? "trilhão" : "trilh�es";
			case 8: return value == 1 ? "milhar de trilhão"  : "milhares de trilhão";
			case 9: return value == 1 ? "quadrilhão"  : "quadrilh�es";
			case 10: return value == 1 ? "milhar de quadrilhão"  : "milhares de quadrilhão";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getInnerGroupConcatenator(int value,int group, int indice) {
		return value <100 && (value%10==0 || value <20) ? null : "e";
	}

	@Override
	public String getInterGroupConcatenator(int previousGroupValue,int previousGroup, int nextGroup) {
		return nextGroup==0 ? "e" : previousGroup > 1 ? "," : "";
	}











}
