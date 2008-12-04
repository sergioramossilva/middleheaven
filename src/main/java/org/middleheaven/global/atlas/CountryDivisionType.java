package org.middleheaven.global.atlas;

public enum CountryDivisionType {

	DIVISION,
	STATE,
	PROVINCE,
	FEDERAL_DISTRICT,
	TERRITORY,
	COUNTY,
	FIRST_LEVEL_DIVISION,
	DEPARTMENT,
	REGION,
	CANTON,
	AUTONOMOUS_REGION,
	DISTRICT,
	CITY,
	PREFECTURE,
	CAPITAL_METROPOLITAN_CITY,
	METROPOLITAN_CITY,
	METROPOLITAN_DEPARTMENT,
	UNION_TERRITORY,
	GROUPS_OF_ISLANDS,
	LAND,
	GOVERNORATE,
	SPECIAL_ADMINISTRATIVE_REGION,
	MUNICIPALITY,
	CYNON,
	AUTONOMOUS_SECTOR;
	
	
	public static CountryDivisionType fromText(String description){
		description = description.trim().toUpperCase().replaceAll(" ", "_").replaceAll("-", "_");
		try{
			return CountryDivisionType.valueOf(description);
		} catch (Exception e){
			if (description.startsWith("CITY")){
				return CountryDivisionType.CITY;
			} else if (description.startsWith("COUNTY")){
				return CountryDivisionType.CITY;
			}  else if (description.endsWith("GROUPS_OF_ISLANDS")){
				return CountryDivisionType.GROUPS_OF_ISLANDS;
			} else if (description.startsWith("PROVINCE")){
				return CountryDivisionType.PROVINCE;
			}else if (description.startsWith("REGION") || description.startsWith("RÉGION")){
				return CountryDivisionType.REGION;
			} 
			return null;
		}
	}
}
