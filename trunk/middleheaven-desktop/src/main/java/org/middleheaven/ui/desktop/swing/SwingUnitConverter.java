package org.middleheaven.ui.desktop.swing;

import java.awt.Container;
import java.awt.FontMetrics;

import org.middleheaven.ui.Displayable;
import org.middleheaven.ui.rendering.UIUnitConverter;

/**
 * {@link UIUnitConverter} implementations for the Swing UI framework.
 */
class SwingUnitConverter extends UIUnitConverter {

	private static SwingUnitConverter me = new SwingUnitConverter();
	
	public static SwingUnitConverter getInstance(){
		return me;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
    protected double[] getDialogBaseUnits(Displayable displayable) {
        Container c;
        if (displayable instanceof Container){
            c = (Container)displayable;
        }  else {
            throw new IllegalArgumentException("Layoutable is not a Container");
        }
        
        FontMetrics metrics = c.getFontMetrics(c.getFont());

        double averageCharWidth = metrics.stringWidth(testString)/  testString.length();
        int    ascent = metrics.getAscent();
        double height = ascent > 14 ? ascent : ascent + (15 - ascent) / 3;
        
        
        return new double[]{averageCharWidth/4, height/8};

    }
}
