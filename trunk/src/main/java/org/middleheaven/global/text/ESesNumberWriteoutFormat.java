package org.middleheaven.global.text;

class ESesNumberWriteoutFormat extends NumberWriteoutFormat{

	public ESesNumberWriteoutFormat(){}
	
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
		return (value==100 || value < 29);
	}

	@Override
	public String getWordsForNotable(int value, int group) {
		switch (value){
		case 1: return "uno";
		case 2: return "dos";
		case 3: return "tres";
		case 4: return "cuatro";
		case 5: return "cinco";
		case 6: return "seis";
		case 7: return "siete";
		case 8: return "ocho";
		case 9: return "nueve";
		case 10: return "diez";
		case 11: return "once";
		case 12: return "doce";
		case 13: return "trece";
		case 14: return "quatorce";
		case 15: return "quince";
		case 16: return "dieciseis";
		case 17: return "diecisiete";
		case 18: return "dieciocho";
		case 19: return "diecinueve";
		case 20: return "veinte";
		case 21: return "veintiuno";
		case 22: return "veintidos";
		case 23: return "veintitres";
		case 24: return "veinticuatro";
		case 25: return "veinticinco";
		case 26: return "veintiseis";
		case 27: return "veintisiete";
		case 28: return "veintiocho";
		case 29: return "veintinueve";
		case 100: return "cien";
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
				case 2: return "veinte";
				case 3: return "treinta";
				case 4: return "cuarenta";
				case 5: return "cincuenta";
				case 6: return "sesenta";
				case 7: return "setenta";
				case 8: return "ochenta";
				case 9: return "noventa";
				default : return "";
				}
			}
		} else if (indice == 2){
			switch (value/100){
			case 1: return "ciento";
			case 2: return "doscientos";
			case 3: return "trescientos";
			case 4: return "cuatrocientos";
			case 5: return "cincocientos";
			case 6: return "seiscientos";
			case 7: return "sietecientos";
			case 8: return "ochocientos";
			case 9: return "novecientos";
			default : return "";
			}
		} else {
			return "";
		}
	}

	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "decimas";
		case 2: return "centesimas";
		case 3: return "milesimas";
		case 4: return "decimas milesimas";
		case 5: return "centesimas milesimas";
		default : return "millionesimas";
		}
	}

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "cero";
			case 2: return "mil";
			case 3: return value == 1 ? "millón" : "millones";
			case 4: return value == 1 ? "billón" : "billones";
			case 5: return value == 1 ? "trillón" : "trllones";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getInnerGroupConcatenator(int value,int group, int indice) {
		return value <100 ? (this.isNotable(value) || value %10 ==0 ?  null : "y") : "";
	}

	@Override
	public String getInterGroupConcatenator(int previousGroupValue,int previousGroup, int nextGroup) {
		return previousGroup==0 ? "y" : "";
	}











}
