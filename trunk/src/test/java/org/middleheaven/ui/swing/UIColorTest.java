package org.middleheaven.ui.swing;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;
import org.middleheaven.ui.UIColor;

public class UIColorTest {

	
	@Test 
	public void testColorRGB(){
		
		Color color = new Color(1,2,3,4);
		
		UIColor uiColor = new UIColor(color.getRGB());
		
		assertEquals(color.getRGB(),uiColor.getRGB());
		
		assertEquals( color.getBlue(), uiColor.getBlue());
		assertEquals( color.getGreen(), uiColor.getGreen());
		assertEquals( color.getRed(), uiColor.getRed());
		
		assertEquals( color.getAlpha(), uiColor.getAlpha());
	}
	
	
	@Test 
	public void testColor(){
		
		Color color = new Color(1,2,3,4);
	
		UIColor uiColor =  UIColor.rgba(1,2,3,4);
		
		assertEquals(color.getRGB(),uiColor.getRGB());
		
		assertEquals( color.getBlue(), uiColor.getBlue());
		assertEquals( color.getGreen(), uiColor.getGreen());
		assertEquals( color.getRed(), uiColor.getRed());
		
		assertEquals( color.getAlpha(), uiColor.getAlpha());
		
	}
	
	@Test
	public void special(){
		UIColor uiColor = UIColor.rgba(1,2,3,4);
		
		assertEquals("010203" , uiColor.rgbToHex());
		assertEquals("04010203" , uiColor.rgbAlphaToHex());
	}
	
	@Test
	public void convertionHVS(){
		
		double[][] values = new double[8][6];
		
		values[0] = new double[]{255,0,0, 0, 1 , 1};
		values[1] = new double[]{0,0,0, 0, 0 , 0};
		values[2] = new double[]{191,191,0, 60, 0.75 , 1};
		values[3] = new double[]{0,127,0, 120, 0.5 , 1};
		values[4] = new double[]{127,255,255, 180, 1 , 0.5};
		values[5] = new double[]{127,127,255, 240, 1 , 0.5};
		values[6] = new double[]{191,63,191, 300, 0.75 , 0.67};
		values[7] = new double[]{161,164,36, 61.41 , 0.64 , 0.78};
		
		
		for (int i = 0 ; i < values.length;i++){
			
			UIColor color = UIColor.rgb( (int)values[i][0] ,(int)values[i][1] ,(int)values[i][2]);
			
			assertDoubleEquals("Hue value for " + i , values[i][3],color.getHue());
			assertDoubleEquals("Value value for " + i , values[i][4],color.getValue());
			assertDoubleEquals("Saturation value for " + i , values[i][5],color.getSaturation());
		
		}
		
		
		
	}
	
	@Test
	public void convertionHVS2(){
		
		UIColor redRGB = UIColor.rgb(255,0,0);
		UIColor redHSV = UIColor.hvs(0, 1, 1);
		
		assertEquals(redRGB, redHSV);
		
		UIColor greenRGB = UIColor.rgb(0,255,0);
		UIColor greenHSV = UIColor.hvs(120, 1, 1);
		
		assertEquals(greenRGB, greenHSV);
		
		UIColor blueRGB = UIColor.rgb(0,0,255);
		UIColor blueHSV = UIColor.hvs(240, 1, 1);
		
		assertEquals(blueRGB, blueHSV);
		
		
	}




	private void assertDoubleEquals(String message, double a, double b) {
		if (Double.compare(a, b)!=0){
			assertEquals(message, a ,b);
		}
	}
}
