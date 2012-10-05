/**
 * 
 */
package org.middleheaven.ui;

/**
 * Represents a display dimension unit
 */
public enum UIDimensionUnit {

   /**
   * Unit representing pixels.
   */
	 PIXELS("px"),
	 /**
	 * Unit representing the font size of the reference font.
	 */
	 EM("em"),
	 /**
	 * Unit representing the x-height of the reference font.
	 */
	 EX("ex"),
	 /**
	 * Unit representing millimeters.
	 */
	 MM("mm"),
	 /**
	 * Unit representing centimeters.
	 */
	 CM("cm"),
	 /**
	 * Unit representing inches.
	 */
	 INCH ("in"),
	 /**
	 * Unit representing points (1/72nd of an inch).
	 */
	 POINTS ("pt"),
	 /**
	 * Unit representing picas (12 points).
	 */
	 PICAS ("pc"),
	 /**
	 * Unit representing in percentage of all available extra space in the containing element
	 * Values from 0, to 1.
	 */
	 RATIO_AVAILABLE ("g"),
	 /**
	 * Unit representing in percentage of the available space in the containing element
	 * Values from 0 to 100.
     */
	 PERCENTAGE("%"),
	 /**
	 * Unit representing the size of the dialog box font.
	 * A horizontal DLU is the average width of the dialog box font divided by four. 
	 * A vertical DLU is the average height of the font divided by eight.
	 * The dialog box is normally 8-point MS Sans Serif.
	 */
	 DIALOG ("dlu");
	 
	 
	 private String symbol;
	 
	 private UIDimensionUnit (String symbol){
		 this.symbol = symbol;
	 }
	 
	 public String getSymbol(){
		 return this.symbol;
	 }
}
