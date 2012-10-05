/**
 * 
 */
package org.middleheaven.util.coersion;

import org.middleheaven.global.text.TextLocalizable;

/**
 * Coercor for {@link String} and {@link TextLocalizable} types.
 */
public class StringTextLocalizableCoersor extends AbstractTypeCoersor<String, TextLocalizable> {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends TextLocalizable> T coerceForward(String value,Class<T> type) {
		return type.cast(TextLocalizable.valueOf(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends String> T coerceReverse(TextLocalizable value,
			Class<T> type) {
		return type.cast(value.toString());
	}



}
