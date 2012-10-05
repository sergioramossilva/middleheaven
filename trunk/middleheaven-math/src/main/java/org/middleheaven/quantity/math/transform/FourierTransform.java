package org.middleheaven.quantity.math.transform;

import java.util.Arrays;

import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.DenseVectorSpaceProvider;
import org.middleheaven.quantity.math.DenseVector;
import org.middleheaven.quantity.math.Vector;

/**
 *  @See http://www.cs.princeton.edu/introcs/97data/FFT.java.html
 */
public class FourierTransform implements Transformation<Vector<Complex>>{
	
	
//    public Vector<Complex> fowardTransform(Vector<Complex> data) {
//    	Complex[] x = new Complex[data.size()];
//    	Complex[]  fx = fowardTransform(data.toArray(x));
//    	
//    	return Vector.vector(fx);
//	}
//    
//	@Override
//	public Vector<Complex> reverseTransform(Vector<Complex> data) {
//		Complex[] x = new Complex[data.size()];
//		Complex[]  fx = reverseTransform(data.toArray(x));
//    	
//    	return Vector.vector(fx);
//	}
    
	// compute the FFT of x[], assuming its length is multiple of 2
    public Vector<Complex> fowardTransform(Vector<Complex> x) {
        int N = x.size();

        // base case
        if (N == 1){
        	return new DenseVector<Complex>(1,x.get(0)); 
        }

        // radix-2 Cooley-Tukey FFT
        if (N % 2 != 0) { 
        	final int nearest2Power = N + N%2;
        	x = pad(x,Complex.ZERO(),nearest2Power);
        }

        // fft of even terms
        
        DenseVector<Complex> even = new DenseVector<Complex>(N/2);
        DenseVector<Complex> odd = new DenseVector<Complex>(N/2);
        
        for (int k = 0; k < N/2; k++) {
            even.set(k , x.get(2*k));
            odd.set(k , x.get(2*k + 1));
        }
        
        Vector<Complex> q = fowardTransform(even);
        Vector<Complex> r = fowardTransform(odd);

        // combine
        DenseVector<Complex> y = new DenseVector<Complex>(N);
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            
            Complex wk = Complex.valueOf(Math.cos(kth), Math.sin(kth));
            
            y.set(k  , q.get(k).plus(wk.times(r.get(k))));
            y.set(k + N/2 , q.get(k).minus(wk.times(r.get(k))));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a multiple of 2
    public Vector<Complex> reverseTransform(Vector<Complex> x) {
        final int N = x.size();
        
        DenseVector<Complex> y = new DenseVector<Complex>(N);

        // take conjugate
        for (int i = 0; i < N; i++) {
            y.set(i , x.get(i).conjugate());
        }

        // compute forward FFT
        y = new DenseVector<Complex>(fowardTransform(y));

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y.set(i , y.get(i).conjugate());
        }

        // divide by N
        for (int i = 0; i < N; i++) {
        	y.set(i , y.get(i).times(1.0 / N));
        }

        return y;

    }

    // compute the circular convolution of x and y
    public Vector<Complex> cconvolve(Vector<Complex>  x, Vector<Complex>  y) {

        // pad x and y with 0s so that they have same length
        // and are multiples of 2
        if (x.size() != y.size()) { 
        	int max = Math.max(x.size(), y.size());
        	if ( max % 2 !=0){
        		max += max %2; 
        	}
        	x = pad(x, Complex.ZERO(), max);
        	y = pad(y, Complex.ZERO(), max);
        }

        // compute FFT of each sequence
        Vector<Complex> a = fowardTransform(x);
        Vector<Complex> b = fowardTransform(y);

        // point-wise multiply
        
        Vector<Complex> c = a.multiply(b);
        
        // compute inverse FFT
        return reverseTransform(c);
    }


    private Vector<Complex> pad(Vector<Complex> original, Complex pad, int max) {
    	
    	Complex[] all = new Complex[original.size()];
    	
    	all = original.toArray(all);
    	
		Complex[] res = Arrays.copyOf(all, max);
		Arrays.fill(res, all.length, max, pad);
		
		return new DenseVectorSpaceProvider().vector(res);
	}


	// compute the linear convolution of x and y
    public Vector<Complex> convolve(Vector<Complex>x, Vector<Complex> y) {
        final Complex ZERO = Complex.ZERO();

        DenseVector<Complex> a = new DenseVector<Complex>(2*x.size());
        
        for (int i = 0;        i <   x.size(); i++){
        	a.set(i , x.get(i));
        }
        for (int i = x.size(); i < 2*x.size(); i++){
        	a.set(i, ZERO);
        }

        DenseVector<Complex> b = new DenseVector<Complex>(2*y.size());
        
        for (int i = 0;        i <   y.size(); i++) {
        	b.set(i , y.get(i));
        }
        for (int i = y.size(); i < 2*y.size(); i++){
        	b.set(i  , ZERO);
        }

        return cconvolve(a, b);
    }







}