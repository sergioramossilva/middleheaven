package org.middleheaven.util;


public class CharacterIncrementor implements Incrementor<Character> {

	int passe;
	public CharacterIncrementor(int passe){
		this.passe = passe;
	}
	
	@Override
	public Character increment(Character c) {
		return new Character((char)(c.charValue()+passe));
	}

	


}
