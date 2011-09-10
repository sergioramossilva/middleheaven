package org.middleheaven.ui.rendering;

import org.middleheaven.ui.Displayable;

public abstract class UIUnitConverter {

    protected static String testString = "X";

    protected UIUnitConverter(){}


    public final int convertVertical(String unitValue, Displayable container ,int availableSpace){
        unitValue = unitValue.toLowerCase();
        int res=0;
        if (unitValue.startsWith("g")){
            res = (int)(availableSpace * Double.parseDouble(unitValue.substring(2,unitValue.length()-1)));
        } else if (unitValue.endsWith("px")){
            res = Integer.parseInt(unitValue.substring(0,unitValue.indexOf("px")));
        } else if (unitValue.endsWith("dlu")){
            res =  (int)Math.round(getDialogBaseUnits(container)[1] * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("dlu")).trim()));
        } else if (unitValue.endsWith("%")){
            res = (int) (availableSpace * 1d * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("%"))) / 100);
        } else if (Character.isDigit(unitValue.charAt(unitValue.length()-1))) {
            res = Integer.parseInt(unitValue);
        }
        return res;
    }

    public final int convertHorizontal(String unitValue, Displayable container , int availableSpace){
        if (unitValue.trim().length()==0){
            throw new IllegalArgumentException("Unitvalue cannot be empty");
        }
        unitValue = unitValue.trim().toLowerCase();
        int res=0;

        if (unitValue.startsWith("g")){
            res = (int)(availableSpace * Double.parseDouble(unitValue.substring(2,unitValue.length()-1)));
        } else if (unitValue.endsWith("px")){
            res = Integer.parseInt(unitValue.substring(0,unitValue.indexOf("px")));
        } else if (unitValue.endsWith("dlu")){
            res = (int)Math.round(getDialogBaseUnits(container)[0] * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("dlu"))));
        } else if (unitValue.endsWith("%")){
            res = (int) (availableSpace * 1d * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("%"))) / 100);
        } else if (unitValue.length()>0 && Character.isDigit(unitValue.charAt(unitValue.length()-1))) {
            res = Integer.parseInt(unitValue);
        }
        return res;
    }

    protected abstract double[] getDialogBaseUnits(Displayable layoutable);

}
