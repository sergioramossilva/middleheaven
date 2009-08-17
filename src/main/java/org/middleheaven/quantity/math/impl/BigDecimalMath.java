package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


class BigDecimalMath {
// from Java Number Cruncher
	
	
	 /**
     * Compute x^exponent to a given scale.  Uses the same
     * algorithm as class numbercruncher.mathutils.IntPower.
     * @param x the value x
     * @param exponent the exponent value
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal intPower(
    		BigDecimal x, long exponent, int scale) {
        // If the exponent is negative, compute 1/(x^-exponent).
        if (exponent < 0) {
            return BigDecimal.valueOf(1)
                        .divide(intPower(x, -exponent, scale), scale,
                                BigDecimal.ROUND_HALF_EVEN);
        }

        BigDecimal power = BigDecimal.valueOf(1);

        // Loop to compute value^exponent.
        while (exponent > 0) {

            // Is the rightmost bit a 1?
            if ((exponent & 1) == 1) {
                power = power.multiply(x)
                          .setScale(scale, BigDecimal.ROUND_HALF_EVEN);
            }

            // Square x and shift exponent 1 bit to the right.
            x = x.multiply(x)
                    .setScale(scale, BigDecimal.ROUND_HALF_EVEN);
            exponent >>= 1;

        }

        return power;
    }

    /**
     * Compute the integral root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param index the integral root value
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal intRoot(BigDecimal x, long index,
                                     int scale){
        // Check that x >= 0.
        if (x.signum() < 0) {
            throw new IllegalArgumentException("x < 0");
        }

        int        sp1 = scale + 1;
        BigDecimal n   = x;
        BigDecimal i   = BigDecimal.valueOf(index);
        BigDecimal im1 = BigDecimal.valueOf(index-1);
        BigDecimal tolerance = BigDecimal.valueOf(5)
                                            .movePointLeft(sp1);
        BigDecimal xPrev;

        // The initial approximation is x/index.
        x = x.divide(i, scale, BigDecimal.ROUND_HALF_EVEN);

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            // x^(index-1)
            BigDecimal xToIm1 = intPower(x, index-1, sp1);

            // x^index
            BigDecimal xToI =
                    x.multiply(xToIm1)
                        .setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

            // n + (index-1)*(x^index)
            BigDecimal numerator =
                    n.add(im1.multiply(xToI))
                        .setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

            // (index*(x^(index-1))
            BigDecimal denominator =
                    i.multiply(xToIm1)
                        .setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

            // x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
            xPrev = x;
            x = numerator
                    .divide(denominator, sp1, BigDecimal.ROUND_DOWN);

        } while (x.subtract(xPrev).abs().compareTo(tolerance) > 0);

        return x;
    }

    /**
     * Compute e^x to a given scale.
     * Break x into its whole and fraction parts and
     * compute (e^(1 + fraction/whole))^whole using Taylor's formula.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal exp(BigDecimal x, int scale)
    {
        // e^0 = 1
        if (x.signum() == 0) {
            return BigDecimal.valueOf(1);
        }

        // If x is negative, return 1/(e^-x).
        else if (x.signum() == -1) {
            return BigDecimal.valueOf(1)
                        .divide(exp(x.negate(), scale), scale,
                                BigDecimal.ROUND_HALF_EVEN);
        }

        // Compute the whole part of x.
        BigDecimal xWhole = x.setScale(0, BigDecimal.ROUND_DOWN);

        // If there isn't a whole part, compute and return e^x.
        if (xWhole.signum() == 0) return expTaylor(x, scale);

        // Compute the fraction part of x.
        BigDecimal xFraction = x.subtract(xWhole);

        // z = 1 + fraction/whole
        BigDecimal z = BigDecimal.valueOf(1)
                            .add(xFraction.divide(
                                    xWhole, scale,
                                    BigDecimal.ROUND_HALF_EVEN));

        // t = e^z
        BigDecimal t = expTaylor(z, scale);

        BigDecimal maxLong = BigDecimal.valueOf(Long.MAX_VALUE);
        BigDecimal result  = BigDecimal.valueOf(1);

        // Compute and return t^whole using intPower().
        // If whole > Long.MAX_VALUE, then first compute products
        // of e^Long.MAX_VALUE.
        while (xWhole.compareTo(maxLong) >= 0) {
            result = result.multiply(
                                intPower(t, Long.MAX_VALUE, scale))
                        .setScale(scale, BigDecimal.ROUND_HALF_EVEN);
            xWhole = xWhole.subtract(maxLong);

        }
        return result.multiply(intPower(t, xWhole.longValue(), scale))
                        .setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Compute e^x to a given scale by the Taylor series.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    private static BigDecimal expTaylor(BigDecimal x, int scale)
    {
        BigDecimal factorial = BigDecimal.valueOf(1);
        BigDecimal xPower    = x;
        BigDecimal sumPrev;

        // 1 + x
        BigDecimal sum  = x.add(BigDecimal.valueOf(1));

        // Loop until the sums converge
        // (two successive sums are equal after rounding).
        int i = 2;
        do {
            // x^i
            xPower = xPower.multiply(x)
                        .setScale(scale, BigDecimal.ROUND_HALF_EVEN);

            // i!
            factorial = factorial.multiply(BigDecimal.valueOf(i));

            // x^i/i!
            BigDecimal term = xPower
                                .divide(factorial, scale,
                                        BigDecimal.ROUND_HALF_EVEN);

            // sum = sum + x^i/i!
            sumPrev = sum;
            sum = sum.add(term);

            ++i;

        } while (sum.compareTo(sumPrev) != 0);

        return sum;
    }

    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     */
    protected static BigDecimal ln(BigDecimal x, int scale)
    {
        // Check that x > 0.
        if (x.signum() <= 0) {
            throw new IllegalArgumentException("x <= 0");
        }

        // The number of digits to the left of the decimal point.
        int magnitude = x.toString().length() - x.scale() - 1;

        if (magnitude < 3) {
            return lnNewton(x, scale);
        }

        // Compute magnitude*ln(x^(1/magnitude)).
        else {

            // x^(1/magnitude)
            BigDecimal root = intRoot(x, magnitude, scale);

            // ln(x^(1/magnitude))
            BigDecimal lnRoot = lnNewton(root, scale);

            // magnitude*ln(x^(1/magnitude))
            return BigDecimal.valueOf(magnitude).multiply(lnRoot)
                        .setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        }
    }

     /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     * Use Newton's algorithm.
     */
    private static BigDecimal lnNewton(BigDecimal x, int scale)
    {
        int        sp1 = scale + 1;
        BigDecimal n   = x;
        BigDecimal term;

        // Convergence tolerance = 5*(10^-(scale+1))
        BigDecimal tolerance = BigDecimal.valueOf(5)
                                            .movePointLeft(sp1);

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {

            // e^x
            BigDecimal eToX = exp(x, sp1);

            // (e^x - n)/e^x
            term = eToX.subtract(n)
                        .divide(eToX, sp1, BigDecimal.ROUND_DOWN);

            // x - (e^x - n)/e^x
            x = x.subtract(term);

        } while (term.compareTo(tolerance) > 0);

        return x.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    // http://www.tc.umn.edu/~ringx004/sidebar.html
    
    static BigDecimal four = BigDecimal.valueOf(4);
	
	public static BigDecimal sin(BigDecimal x, int scale) {

        // If x is negative, return -sin(-x).
        if (x.signum() == -1) {
            return sin(x.negate(), scale).negate();
        } else {
        	
        	if (x.compareTo(BigDecimal.ZERO) == 0 || x.compareTo(BigDecimal.valueOf(Math.PI)) == 0){
        		return BigDecimal.ZERO;
        	} else if (x.compareTo(BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(2))) == 0){
        		return BigDecimal.ONE;
        	}
        	
        	x = x.remainder(BigDecimal.valueOf(2*Math.PI));

        	
        	// |x| > 1
        	if (x.compareTo(new BigDecimal("0.1257")) > 0){
        		BigDecimal y = x.divide(BigDecimal.valueOf(5),BigDecimal.ROUND_HALF_EVEN);
        		BigDecimal s = sin(y,scale);
        		BigDecimal s3 = s.multiply(s).multiply(s);
        		BigDecimal s5 = s3.multiply(s).multiply(s);
        		return BigDecimal.valueOf(16).multiply(s5).subtract(BigDecimal.valueOf(20).multiply(s3))
        		.add(BigDecimal.valueOf(5).multiply(s)).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        	}
        	
        	
            return sinTaylor(x, scale);
        }
	}
	
	 private static BigDecimal sinTaylor(BigDecimal x, int scale){
	        int     sp1     = scale + 1;
	        int     i       = 3;
	        boolean addFlag = false;

	        BigDecimal power = x;
	        BigDecimal sum   = x;
	        BigDecimal term;

	        // Convergence tolerance = (10^-(scale+1))
	        BigDecimal tolerance = BigDecimal.ONE.movePointLeft(sp1);

	        
	        // taylor is 1- x^3/3! + x^5/5! - x^7/7! 
	        // Loop until the approximations converge
	        // (two successive approximations are within the tolerance).
	
	        do {
	            // x^i
	            power = x.pow(i);

	            // (x^i)/i!
	            term = power.divide(factorial(BigDecimal.valueOf(i)), sp1, BigDecimal.ROUND_HALF_EVEN);

	            // sum = sum +- (x^i)/i!
	            sum = addFlag ? sum.add(term) : sum.subtract(term);

	            i += 2;
	            addFlag = !addFlag;

	        } while (term.compareTo(tolerance) > 0);

	       // sum = sum.(scale,BigDecimal.ROUND_HALF_EVEN);
	        return sum;
	    }
	
    /**
     * Compute the cos of x to a given scale, |x| < 1
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal cos(BigDecimal x, int scale){

        // If x is negative, return cos(-x).
        if (x.signum() == -1) {
            return cos(x.negate(), scale);
        } else {
        	
        	if (x.compareTo(BigDecimal.ZERO) == 0 ){ 
        		return BigDecimal.ONE;
        	} else if(x.compareTo(BigDecimal.valueOf(Math.PI)) == 0){
        		return BigDecimal.ONE.negate();
        	} else if (x.compareTo(BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(2))) == 0){
        		return BigDecimal.ZERO;
        	}
        	
        	x = x.remainder(BigDecimal.valueOf(2*Math.PI));
        	  
        	// |x| > 1
        	if (x.compareTo(new BigDecimal("0.015")) > 0){
        		BigDecimal y = x.divide(BigDecimal.valueOf(4),BigDecimal.ROUND_HALF_EVEN);
        		BigDecimal c = cos(y,scale);
        		BigDecimal c2 = c.multiply(c);
        		BigDecimal c4 = c2.multiply(c2);
        		return BigDecimal.valueOf(8).multiply(c4.subtract(c2)).add(BigDecimal.ONE)
        					.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        	}
        	
            return cosTaylor(x, scale);
        }
    }
    
    static Map<BigDecimal, BigDecimal> factors = new HashMap<BigDecimal, BigDecimal>();
    
    static {
    	factors.put(BigDecimal.ZERO, BigDecimal.ONE);
    	factors.put(BigDecimal.ONE, BigDecimal.ONE);
    	factors.put(BigDecimal.valueOf(2), BigDecimal.valueOf(2));
    	
    	factorial(BigDecimal.valueOf(12)); //fill the map
    }
    
    private static BigDecimal factorial (BigDecimal x){
    	if(x.signum() < 0 ){
    		throw new IllegalArgumentException("Factorial is for positive values");
    	} else {
    		BigDecimal result = factors.get(x);
    		if (result == null){
    			result = x.multiply(factorial(x.subtract(BigDecimal.ONE)));
    			factors.put(x, result);
    		}
    		return result;
    	}
    }
    

    private static BigDecimal cosTaylor(BigDecimal x, int scale){
        int     sp1     = scale + 1;
        int     i       = 2;
        boolean addFlag = false;

        BigDecimal power = x;
        BigDecimal sum   = BigDecimal.ONE;
        BigDecimal term;

        // Convergence tolerance = (10^-(scale+1))
        BigDecimal tolerance = BigDecimal.ONE.movePointLeft(sp1);

        
        // taylor is 1-x^2/2! + x^4/4! - x^6/6! 
        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // x^i
            power = x.pow(i);

            // (x^i)/i!
            term = power.divide(factorial(BigDecimal.valueOf(i)), sp1, BigDecimal.ROUND_HALF_EVEN);

            // sum = sum +- (x^i)/i!
            sum = addFlag ? sum.add(term) : sum.subtract(term);

            i += 2;
            addFlag = !addFlag;

        } while (term.compareTo(tolerance) > 0);

        return sum;
    }
    
    /**
     * Compute the arctangent of x to a given scale, |x| < 1
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal arctan(BigDecimal x, int scale) {
        // Check that |x| < 1.
        if (x.abs().compareTo(BigDecimal.valueOf(1)) >= 0) {
            throw new IllegalArgumentException("|x| >= 1");
        }

        // If x is negative, return -arctan(-x).
        if (x.signum() == -1) {
            return arctan(x.negate(), scale).negate();
        } else {
            return arctanTaylor(x, scale);
        }
    }

    /**
     * Compute the arctangent of x to a given scale
     * by the Taylor series, |x| < 1
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    private static BigDecimal arctanTaylor(BigDecimal x, int scale) {
        int     sp1     = scale + 1;
        int     i       = 3;
        boolean addFlag = false;

        BigDecimal power = x;
        BigDecimal sum   = x;
        BigDecimal term;

        // Convergence tolerance = 5*(10^-(scale+1))
        BigDecimal tolerance = BigDecimal.valueOf(5)
                                            .movePointLeft(sp1);

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // x^i
            power = power.multiply(x).multiply(x)
                        .setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

            // (x^i)/i
            term = power.divide(BigDecimal.valueOf(i), sp1,
                                 BigDecimal.ROUND_HALF_EVEN);

            // sum = sum +- (x^i)/i
            sum = addFlag ? sum.add(term)
                          : sum.subtract(term);

            i += 2;
            addFlag = !addFlag;

        } while (term.compareTo(tolerance) > 0);

        return sum;
    }

    /**
     * Compute the square root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    protected static BigDecimal sqrt(BigDecimal x, int scale){
        // Check that x >= 0.
        if (x.signum() < 0) {
            throw new IllegalArgumentException("x < 0");
        }

        // n = x*(10^(2*scale))
        BigInteger n = x.movePointRight(scale << 1).toBigInteger();

        // The first approximation is the upper half of n.
        int bits = (n.bitLength() + 1) >> 1;
        BigInteger ix = n.shiftRight(bits);
        BigInteger ixPrev;

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            ixPrev = ix;

            // x = (x + n/x)/2
            ix = ix.add(n.divide(ix)).shiftRight(1);

        } while (ix.compareTo(ixPrev) != 0);

        return new BigDecimal(ix, scale);
    }



    
}
