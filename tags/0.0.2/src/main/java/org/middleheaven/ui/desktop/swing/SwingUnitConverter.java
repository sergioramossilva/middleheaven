package org.middleheaven.ui.desktop.swing;

import java.awt.Container;
import java.awt.FontMetrics;

import org.middleheaven.ui.Displayable;
import org.middleheaven.ui.rendering.UIUnitConverter;


public class SwingUnitConverter extends UIUnitConverter {

    protected double[] getDialogBaseUnits(Displayable layoutable) {
        Container c;
        if (layoutable instanceof Container){
            c = (Container)layoutable;
        } else if ( layoutable instanceof SwingUIAreaAdapter){
            c = ((SwingUIAreaAdapter)layoutable).getContainer();
        } else {
            throw new IllegalArgumentException("Layoutable is not a Container");
        }
        
        FontMetrics metrics = c.getFontMetrics(c.getFont());

        double averageCharWidth = metrics.stringWidth(testString)/  testString.length();
        int    ascent = metrics.getAscent();
        double height = ascent > 14 ? ascent : ascent + (15 - ascent) / 3;
        
        
        return new double[]{averageCharWidth/4, height/8};

    }
}
