package org.middleheaven.util;

/**
 * Incrementor for {@link Character}.
 * 
 */
public class CharacterIncrementor implements Incrementor<Character> {

	int passe;
	
	/**
	 * 
	 * Constructor.
	 * @param passe
	 */
	public CharacterIncrementor(int passe){
		this.passe = passe;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Character increment(Character c) {
		return Character.valueOf((char)(c.charValue()+passe));
	}

	


}
