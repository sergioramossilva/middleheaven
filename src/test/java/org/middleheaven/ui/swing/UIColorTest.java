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
		
		UIColor uiColor = new UIColor(1,2,3,4);
		
		assertEquals(color.getRGB(),uiColor.getRGB());
		
		assertEquals( color.getBlue(), uiColor.getBlue());
		assertEquals( color.getGreen(), uiColor.getGreen());
		assertEquals( color.getRed(), uiColor.getRed());
		
		assertEquals( color.getAlpha(), uiColor.getAlpha());
		
	}
	
	@Test
	public void special(){
		UIColor uiColor = new UIColor(1,2,3,4);
		
		assertEquals("010203" , uiColor.rgbToHex());
		assertEquals("04010203" , uiColor.rgbAlphaToHex());
	}
}
