
/**
 * A culture independent writout toolbox.
 * 
 * This toolbox also contains some culture dependent without implementations
 * for European Portuguese , Brazilian Portuguese , Great Britain English,
 * Spain spanish and France french. 
 * 
 * Example:
 * 
 * For Portugal-Portuguese we can write numbers using the following code:
 * {@code
 * 	NumberWriteoutFormat format = NumberWriteoutFormat.getInstance(Culture.valueOf("pt","PT"));
 *	
 *  assertEquals("zero", format.inWords(0));
 *	assertEquals("cem", format.inWords(100));
 * }
 * 
 * Future plans includ writeout Money values for use in financial printing
 */
package org.middleheaven.global.text.writeout;

