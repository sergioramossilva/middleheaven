package org.middleheaven.quantity.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.math.transform.FourierTransform;
import org.middleheaven.util.collections.Walker;


public class FFTTest {

	VectorSpaceProvider provider = new DenseVectorSpaceProvider();

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


		Vector<Complex> x = provider.vector(
				Complex.real( -0.03480425839330703),
				Complex.real(	0.07910192950176387),
				Complex.real( 0.7233322451735928),
				Complex.real( 0.1659819820667019)
				);

		// original data
		show(x, "x");

		FourierTransform ft = new FourierTransform();

		// FFT of original data
		Vector<Complex> y = ft.fowardTransform(x);
		show(y, "y = fft(x)");

		// take inverse FFT
		Vector<Complex> z = ft.reverseTransform(y);
		show(z, "z = ifft(y)");

		// TODO define MathContext
		assertVectorsEquals(x,z);

		// circular convolution of x with itself
		Vector<Complex> c = ft.cconvolve(x, x);
		show(c, "c = cconvolve(x, x)");

		// linear convolution of x with itself
		Vector<Complex> d = ft.convolve(x, x);
		show(d, "d = convolve(x, x)");
	}

	/**
	 * @param x
	 * @param z
	 * @return
	 */
	private boolean equals(Vector<Complex> x, Vector<Complex> z) {
		return x.equals(z);
	}

	/**
	 * @return
	 */
	private Object DenseMatrixProvider() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	@Test
	public void testFFTVector(){

		Vector<Complex> x = provider.vector(
				Complex.real( -0.03480425839330703),
				Complex.real(	0.07910192950176387),
				Complex.real( 0.7233322451735928),
				Complex.real( 0.1659819820667019)
				);

		FourierTransform ft = new FourierTransform();

		// FFT of original data
		Vector<Complex> y = ft.fowardTransform(x);

		// take inverse FFT
		Vector<Complex> z = ft.reverseTransform(y);


		assertVectorsEquals(x, z );
	}

	/**
	 * @param x
	 * @param z
	 */
	private void assertVectorsEquals(Vector<Complex> x, Vector<Complex> z) {

		final Real epslon = Real.valueOf(1E-30);
		
		Vector<Complex> diff = x.minus(z);

		diff.forEach(new Walker<Complex>(){

			@Override
			public void doWith(Complex r) {
				assertTrue("Real part" + r.toReal() + " is not less than 1E-15",  r.toReal().compareTo(epslon) <=0);
				assertTrue("Imaginary part" + r.toImaginary() + " is not less than 1E-15",  r.toImaginary().compareTo(epslon) <=0);
			}
			
		});
	
			

	}

	@Test
	public void testFFTChange(){

		Vector<Complex> x = provider.vector(
				Complex.real( -0.03480425839330703),
				Complex.real(	0.07910192950176387),
				Complex.real( 0.7233322451735928),
				Complex.real( 0.1659819820667019)
				);


		// original data
		show(x, "x");
		FourierTransform ft = new FourierTransform();
		FourierTransform ft2 = new FourierTransform();

		// FFT of original data
		Vector<Complex> y = ft.fowardTransform(x);
		Vector<Complex> y2 = ft.fowardTransform(x);

		assertTrue(equals(y,y2));

	}

	// display an array of Complex numbers to standard output
	public static void show(Vector<Complex> x, String title) {
		//       System.out.println(title);
		//        System.out.println("-------------------");
		//        for (int i = 0; i < x.length; i++) {
		//            System.out.println(x[i]);
		//        }
		//        System.out.println();
	}
}
