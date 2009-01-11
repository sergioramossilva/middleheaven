package org.middleheaven.ui;

import java.io.Serializable;

public final class UIColor implements Serializable {

	private static final long serialVersionUID = 8360328480399107467L;

	private static final double FACTOR = 0.7;

	private int argb;

	public UIColor(int r, int g, int b){
		this(r,g,b,255);
	}

	public UIColor(int r, int g, int b, int alpha){
		testColorValueRange(r,g,b,alpha);
		argb = b | g << 8 | r << 16 | alpha << 24;

	}
	
	public UIColor(int argb){
		this.argb = argb;
	}
	
	public int [] getRGBComponents(){
		return new int[]{this.getRed(), this.getGreen(), this.getBlue()};
	}

	public int [] getRGBAlphaComponents(){
		return new int[]{this.getRed(), this.getGreen(), this.getBlue(),this.getAlpha()};
	}

	public UIColor darker(){
		return new UIColor(
				Math.max((int)(getRed()  *FACTOR), 0),
				Math.max((int)(getGreen()*FACTOR), 0),
				Math.max((int)(getBlue() *FACTOR), 0)
		);

	}

	public UIColor brighter(){

		int inverse = (int)(1.0/(1.0-FACTOR));

		// if is black
		if ( (argb & 0x00FFFFFF) == 0) {
			return new UIColor(inverse, inverse, inverse);
		}


		int r = getRed();
		int g = getGreen();
		int b = getBlue();



		if ( r > 0 && r < inverse ) r = inverse;
		if ( g > 0 && g < inverse ) g = inverse;
		if ( b > 0 && b < inverse ) b = inverse;

		return new UIColor(
				Math.min((int)(r/FACTOR), 255),
				Math.min((int)(g/FACTOR), 255),
				Math.min((int)(b/FACTOR), 255)
		);

	}

	private static void testColorValueRange(int r, int g, int b, int a) {
		boolean rangeError = false;
		String badComponentString = "";

		if ( a < 0 || a > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Alpha";
		}
		if ( r < 0 || r > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Red";
		}
		if ( g < 0 || g > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Green";
		}
		if ( b < 0 || b > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Blue";
		}
		if ( rangeError == true ) {
			throw new IllegalArgumentException("Color parameter outside of expected range:"
					+ badComponentString);
		}
	}




	public boolean equals(Object other){
		return other instanceof UIColor && ((UIColor)other).argb == this.argb;
	}
	
	public int hashCode(){
		return argb;
	}
	
	/**
	 * Returns the RGB value representing the color in the default RGB . 
	 * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue). 
	 *
	 */
	public int getRGB(){
		return argb;
	}

	public int getBlue(){
		return argb & 0xFF;
	}

	public int getGreen(){
		return argb >> 8 & 0xFF;
	}

	public int getRed(){
		return argb >> 16 & 0xFF;
	}

	public int getAlpha(){
		return argb >> 24 & 0xFF;
	}
	
	public UIColor opaque(){
		return new UIColor(this.getRed(),this.getGreen(),this.getBlue());
	}
	
	public UIColor transparent(){
		return new UIColor(this.getRed(),this.getGreen(),this.getBlue(), 0);
	}

	public String toString(){
		return new StringBuilder("[")
		.append("r=").append(getRed())
		.append(", g=").append(getGreen())
		.append(", b=").append(getBlue())
		.append(", a=").append(getAlpha())
		.append("]")
		.toString();

	}

	public String rgbToHex(){
		String s = Integer.toHexString(this.argb & 0x00FFFFFF);
		return s.length()<6  ? "0".concat(s) : s;
	}

	public String rgbAlphaToHex(){
		String s =  Integer.toHexString(this.argb);
		return s.length()<8  ? "0".concat(s) : s;
	}
}
