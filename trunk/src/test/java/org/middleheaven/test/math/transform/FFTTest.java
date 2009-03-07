package org.middleheaven.test.math.transform;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.transforms.FourierTransform;
import org.middleheaven.quantity.structure.Vector;


public class FFTTest {

	
	   /*********************************************************************
	    *  Test client and sample execution
	    *
	    *  % java FFT 4
	    *  x
	    *  -------------------
	    *  -0.03480425839330703
	    *  0.07910192950176387
	    *  0.7233322451735928
	    *  0.1659819820667019
	    *
	    *  y = fft(x)
	    *  -------------------
	    *  0.9336118983487516
	    *  -0.7581365035668999 + 0.08688005256493803i
	    *  0.44344407521182005
	    *  -0.7581365035668999 - 0.08688005256493803i
	    *
	    *  z = ifft(y)
	    *  -------------------
	    *  -0.03480425839330703
	    *  0.07910192950176387 + 2.6599344570851287E-18i
	    *  0.7233322451735928
	    *  0.1659819820667019 - 2.6599344570851287E-18i
	    *
	    *  c = cconvolve(x, x)
	    *  -------------------
	    *  0.5506798633981853
	    *  0.23461407150576394 - 4.033186818023279E-18i
	    *  -0.016542951108772352
	    *  0.10288019294318276 + 4.033186818023279E-18i
	    *
	    *  d = convolve(x, x)
	    *  -------------------
	    *  0.001211336402308083 - 3.122502256758253E-17i
	    *  -0.005506167987577068 - 5.058885073636224E-17i
	    *  -0.044092969479563274 + 2.1934338938072244E-18i
	    *  0.10288019294318276 - 3.6147323062478115E-17i
	    *  0.5494685269958772 + 3.122502256758253E-17i
	    *  0.240120239493341 + 4.655566391833896E-17i
	    *  0.02755001837079092 - 2.1934338938072244E-18i
	    *  4.01805098805014E-17i
	    *
	    *********************************************************************/


	
	@Test
	public void testFFT(){
			int N = 4;
	        Complex[] x = new Complex[N];

	        x[0] = Complex.valueOf( -0.03480425839330703 , 0);
	        x[1] = Complex.valueOf(	0.07910192950176387 , 0);
	        x[2] = Complex.valueOf( 0.7233322451735928 , 0);
	        x[3] = Complex.valueOf( 0.1659819820667019 , 0);

	        // original data
	        show(x, "x");

	        FourierTransform ft = new FourierTransform();
	        
	        // FFT of original data
	        Complex[] y = ft.fowardTransform(x);
	        show(y, "y = fft(x)");

	        // take inverse FFT
	        Complex[] z = ft.reverseTransform(y);
	        show(z, "z = ifft(y)");

	        assertTrue(Arrays.equals(x,z));
	        
	        // circular convolution of x with itself
	        Complex[] c = ft.cconvolve(x, x);
	        show(c, "c = cconvolve(x, x)");

	        // linear convolution of x with itself
	        Complex[] d = ft.convolve(x, x);
	        show(d, "d = convolve(x, x)");
	}
	
	@Test
	public void testFFTVector(){
			int N = 4;
	        Complex[] x = new Complex[N];

	        x[0] = Complex.valueOf( -0.03480425839330703 , 0);
	        x[1] = Complex.valueOf(	0.07910192950176387 , 0);
	        x[2] = Complex.valueOf( 0.7233322451735928 , 0);
	        x[3] = Complex.valueOf( 0.1659819820667019 , 0);

	        Vector<Complex> v = Vector.vector(x); 
	       
	        FourierTransform ft = new FourierTransform();
	        
	        // FFT of original data
	        Vector<Complex> y = ft.fowardTransform(v);
	     
	        // take inverse FFT
	        Vector<Complex> z = ft.reverseTransform(y);

	        assertTrue(v.equals(z));
	}
	
	@Test
	public void testFFTChange(){
			int N = 4;
	        Complex[] x = new Complex[N];

	        x[0] = Complex.valueOf( -0.03480425839330703 , 0);
	        x[1] = Complex.valueOf(	0.07910192950176387 , 0);
	        x[2] = Complex.valueOf( 0.7233322451735928 , 0);
	        x[3] = Complex.valueOf( 0.1659819820667019 , 0);

	        // original data
	        show(x, "x");
	        FourierTransform ft = new FourierTransform();
	        FourierTransform ft2 = new FourierTransform();
	        
	        // FFT of original data
	        Complex[] y = ft.fowardTransform(x);
	        Complex[] y2 = ft.fowardTransform(x);
	        
	        assertTrue(Arrays.equals(y,y2));

	}
	
    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
//       System.out.println(title);
//        System.out.println("-------------------");
//        for (int i = 0; i < x.length; i++) {
//            System.out.println(x[i]);
//        }
//        System.out.println();
    }
}
