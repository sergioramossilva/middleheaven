package org.middleheaven.util;

/**
 * Incrementor for {@link Character}.
 * 
 */
public class CharacterIncrementor implements Incrementor<Character> {

	int step;
	
	/**
	 * 
	 * Constructor.
	 * @param step
	 */
	public CharacterIncrementor(int step){
		this.step = step;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Character increment(Character c) {
		return Character.valueOf((char)(c.charValue()+step));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<Character> reverse() {
		return new CharacterIncrementor(-this.step);
	}

	


}
