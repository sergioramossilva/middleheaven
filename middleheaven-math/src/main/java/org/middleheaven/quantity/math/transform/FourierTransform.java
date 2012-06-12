package org.middleheaven.quantity.math.transform;

import java.util.Arrays;

import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Vector;

/**
 *  @See http://www.cs.princeton.edu/introcs/97data/FFT.java.html
 */
public class FourierTransform implements Transformation<Vector<Complex>>{
	
	
    public Vector<Complex> fowardTransform(Vector<Complex> data) {
    	Complex[] x = new Complex[data.size()];
    	Complex[]  fx = fowardTransform(data.toArray(x));
    	
    	return Vector.vector(fx);
	}
    
	@Override
	public Vector<Complex> reverseTransform(Vector<Complex> data) {
		Complex[] x = new Complex[data.size()];
		Complex[]  fx = reverseTransform(data.toArray(x));
    	
    	return Vector.vector(fx);
	}
    
	// compute the FFT of x[], assuming its length is multiple of 2
    public Complex[] fowardTransform(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix-2 Cooley-Tukey FFT
        if (N % 2 != 0) { 
        	final int nearest2Power = N + N%2;
        	x = pad(x,Complex.ZERO(),nearest2Power);
        }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        Complex[] odd = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
            odd[k] = x[2*k + 1];
        }
        
        Complex[] q = fowardTransform(even);
        Complex[] r = fowardTransform(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = Complex.valueOf(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a multiple of 2
    public Complex[] reverseTransform(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fowardTransform(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public Complex[] cconvolve(Complex[] x, Complex[] y) {

        // pad x and y with 0s so that they have same length
        // and are multiples of 2
        if (x.length != y.length) { 
        	int max = Math.max(x.length, y.length);
        	if ( max % 2 !=0){
        		max += max %2; 
        	}
        	x = pad(x, Complex.ZERO(), max);
        	y = pad(y, Complex.ZERO(), max);
        }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fowardTransform(x);
        Complex[] b = fowardTransform(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return reverseTransform(c);
    }


    private Complex[] pad(Complex[] original, Complex pad, int max) {
		Complex[] res = Arrays.copyOf(original, max);
		Arrays.fill(res, original.length, max, pad);
		return res;
	}


	// compute the linear convolution of x and y
    public Complex[] convolve(Complex[] x, Complex[] y) {
        final Complex ZERO = Complex.ZERO();

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }







}