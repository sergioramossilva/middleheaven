package org.middleheaven.ui;

import java.io.Serializable;

public final class UIColor implements Serializable {

	private static final long serialVersionUID = 8360328480399107467L;

	private static final double FACTOR = 0.7;

	private int argb;

	public static UIColor white(){
		return rgba(255,255,255,255);
	}
	
	public static UIColor black(){
		return rgba(0,0,0,255);
	}
	
	public static UIColor rgb(int r, int g, int b){
		return rgba(r,g,b,255);
	}
	
	public static UIColor hvs(double hue, double value, double saturation){

		// see http://en.wikipedia.org/wiki/HSL_color_space
		double h = hue / 60;
		double C = value * saturation;
		double x = C * (1 - Math.abs(h % 2  - 1));
		
		double[] rgb = new double[]{0,0,0};
		
		if (h >= 0 && h < 1){
			 rgb = new double[]{C,x,0};
		} else if (h >= 1 && h < 2){
			 rgb = new double[]{x,C,0};
		} else if (h >= 2 && h < 3){
			 rgb = new double[]{0,C,x};
		} else if (h >= 3 && h < 4){
			 rgb = new double[]{0,x,C};
		} else if (h >=4 && h < 5){
			 rgb = new double[]{x,0,C};
		} else {
			 rgb = new double[]{C,0,x};
		}
		double m = value - C;
		
		int[] irgb = new int [3];
		
		for (int i =0; i < 3;i++){
			irgb[i] = (int)(255* (rgb[i] + m));
		}
		
		return rgb(irgb[0],irgb[1],irgb[2]);
	}
	
	
	public static UIColor rgba(int r, int g, int b, int alpha){
		return new UIColor(r,g,b,alpha);
	}
	
	private UIColor(int r, int g, int b, int alpha){
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

	public UIColor grayScale(){
		
		int gray = (this.getRed() + this.getGreen() + this.getGreen()) /3;
		
		return rgba(gray, gray, gray, this.getAlpha());
	}
	
	public UIColor darker(){
		return new UIColor(
				Math.max((int)(getRed()  *FACTOR), 0),
				Math.max((int)(getGreen()*FACTOR), 0),
				Math.max((int)(getBlue() *FACTOR), 0),
				this.getAlpha()
		);

	}

	public UIColor brighter(){

		int inverse = (int)(1.0/(1.0-FACTOR));

		// if is black
		if ( (argb & 0x00FFFFFF) == 0) {
			return new UIColor(inverse, inverse, inverse,this.getAlpha());
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
				Math.min((int)(b/FACTOR), 255),
				this.getAlpha()
		);

	}

	private static void testColorValueRange(int r, int g, int b, int a) {
		boolean rangeError = false;
		String badComponentString = "";
		int badValue =0;
		
		if ( a < 0 || a > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Alpha";
			badValue = a;
		}
		if ( r < 0 || r > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Red";
			badValue = r;
		}
		if ( g < 0 || g > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Green";
			badValue = g;
		}
		if ( b < 0 || b > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Blue";
			badValue = b;
		}
		if ( rangeError == true ) {
			throw new IllegalArgumentException("Color parameter outside of expected range:"
					+ badComponentString + "(" + badValue + ")");
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
		return new UIColor(this.getRed(),this.getGreen(),this.getBlue(),255);
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
	
	
	public double getValue(){
		
		return round(Math.max(Math.max(this.getRed(),this.getGreen()), this.getBlue())/255d);
	}
	
	private double round(double value) {
		double base = ((int)(value * 100))/ 100d;
		int d = (int)(((int)(value * 1000)) / 10);
		int s = (int)(value * 1000) - (d * 10);
		if ( s >= 5 ){
			return base + 0.01;
		} else {
			return base;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 * @see http://en.wikipedia.org/wiki/HSL_color_space
	 */
	public double getHue(){
		double r = this.getRed()/255d;
		double g = this.getGreen()/255d;
		double b = this.getBlue()/255d;
		
		/*
		double a = 0.5 * (2 * r  - g - b);
		double beta = Math.cbrt(3) * (g - b) / 2;
		
		return Math.toDegrees(Math.atan2(beta, a));
		*/
		
		double M = Math.max(Math.max(r, g), b);
		double m = Math.min(Math.min(r, g), b);
		double C = M-m;
		
		double h;
		
		if (Double.compare(C, 0) == 0){
			return 0;
		} else if ( M == r){
			h =  60 * (((g-b)/ C) % 6);
		} else if ( M == g){
			h = 60 * ((b-r) / C + 2);   
		}else if ( M == b){
			h = 60 * ((r-g) / C + 4);   
		} else {
			return 0;
		}
		
		if ( h < 0 ){
			h +=360;
		}
		return round(h);
	}
	
	public double getSaturation(){
		double r = this.getRed()/255d;
		double g = this.getGreen()/255d;
		double b = this.getBlue()/255d;
		
//		double a = 0.5 * (2 * r - g - b);
//		double beta = Math.cbrt(3) * (g - b) / 2;
//
//		double C = Math.hypot(a, beta);
//		
		
		double M = Math.max(Math.max(r, g), b);
		double m = Math.min(Math.min(r, g), b);
		double C = M-m;
		
		if (Double.compare(C, 0) == 0 ) {
			return 0;
		} else {
			return round((C / this.getValue()));
		}
	}

}
