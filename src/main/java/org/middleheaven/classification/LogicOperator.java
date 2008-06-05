/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.classification;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class LogicOperator {

    static final LogicOperator AND = new AndOperator();
    static final LogicOperator OR = new OrOperator();
    static final LogicOperator XOR = new XorOperator();
    
    public static LogicOperator or(){
    	return OR;
    }
    
    public static LogicOperator and(){
    	return AND;
    }
    
    public static LogicOperator xor(){
    	return XOR;
    }
    

    private String name;
    
    private LogicOperator(String name){
    	this.name = name;   	 
    }
    
    public String toString() {
		return name;
	}

    public abstract boolean isBreakValue(boolean value);
    public abstract boolean operate(boolean a, boolean b);
	
    
    public boolean equals(Object other){
        return other instanceof LogicOperator && other.toString().equals(this.name);
    }
    
    /* AND Operator implementation*/
    private static class AndOperator extends LogicOperator {

    	public AndOperator(){
    		super("AND");
    	}
    	
        @Override
        public boolean isBreakValue(boolean value) {
            return !value; // break if the value is false
        }

        @Override
        public boolean operate(boolean a, boolean b) {
            return a && b;
        }
        
    }
    
    /* OR Operator implementation*/
    private static class OrOperator extends LogicOperator {

    	public OrOperator(){
    		super("OR");
    	}
    	
        @Override
        public boolean isBreakValue(boolean value) {
            return value; // break if the value is true
        }

        @Override
        public boolean operate(boolean a, boolean b) {
            return a || b;
        }
        
    }
    

    /* XOR Operator implementation*/
    private static class XorOperator extends LogicOperator {

    	public XorOperator(){
    		super("XOR");
    	}
    	
        @Override
        public boolean isBreakValue(boolean value) {
            return false; // there is no break value for XOR
        }

        @Override
        public boolean operate(boolean a, boolean b) {
            return a ^ b;
        }
        
    }





}
