package org.middleheaven.global.location;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.coordinate.GeoCoordinate;
import org.middleheaven.util.measure.measures.Distance;


public class WGM84 extends ElipsoideModel{

	public WGM84() {
		super(//http://en.wikipedia.org/wiki/WGS84#A_new_World_Geodetic_System:_WGS_84
				DecimalMeasure.exact(Real.valueOf("6378137.0"), SI.METER), 
				DecimalMeasure.exact(Real.valueOf("6356752.3142"), SI.METER), 
				DecimalMeasure.exact(Real.valueOf("298.257223563").inverse() , SI.DIMENTIONLESS)
		);
	}

	private double reduceLatitude(AngularPosition latitude){
		return atan((1- this.getFlatening().amount().asNumber().doubleValue()) * latitude.toRadians().tan().asNumber().doubleValue());
	}

	@Override
	public DecimalMeasure<Distance> distance(GeoCoordinate c1, GeoCoordinate c2) {
		return DecimalMeasure.exact(Real.valueOf(distance1(c1,c2)), this.getSemiMajorAxis().unit());
	}

	private double distance1(GeoCoordinate c1, GeoCoordinate c2){
		// http://www.movable-type.co.uk/scripts/latlong-vincenty.html

		final double a = this.getSemiMajorAxis().amount().asNumber().doubleValue();
		final double b = this.getSemiMinorAxis().amount().asNumber().doubleValue();
		final double f = this.getFlatening().amount().asNumber().doubleValue();

		final double L = c1.getLongitude().minus(c2.getLongitude()).toRadians().amount().asNumber().doubleValue();
		final double U1 = reduceLatitude(c1.getLatitude()); 	 
		final double U2 = reduceLatitude(c2.getLatitude()); 

		final double sinU1 = sin(U1), cosU1 = cos(U1);
		final double sinU2 = sin(U2), cosU2 = cos(U2);

		double lambda = L, lambdaP = 2*PI;
		double iterLimit = 20;
		double cosSqAlpha=0;
		double sinSigma =0;
		double cos2SigmaM = 0;
		double cosSigma = 0;
		double sigma=0;
		double sinLambda=0;
		double cosLambda=0;
		while (abs(lambda-lambdaP) > 1e-12 && --iterLimit>0) {
		   sinLambda = sin(lambda);
		   cosLambda = cos(lambda);
		   
		   sinSigma = hypot((cosU2*sinLambda) ,  (cosU1*sinU2-sinU1*cosU2*cosLambda)); // h = sqrt(x^2 + y^2)
			
		   if (sinSigma==0) return 0;  // co-incident points
		   
			cosSigma = sinU1*sinU2 + cosU1*cosU2*cosLambda;
		    sigma = atan2(sinSigma, cosSigma);
			double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
		    cosSqAlpha = 1 - sinAlpha*sinAlpha;
			 // equatorial line: cosSqAlpha=0 
		    cos2SigmaM = cosSqAlpha ==0 ? 0 : (cosSigma - 2*sinU1*sinU2/cosSqAlpha);
			double C = f/16*cosSqAlpha*(4+f*(4-3*cosSqAlpha));
			lambdaP = lambda;
			lambda = L + (1-C) * f * sinAlpha * (sigma + C*sinSigma*(cos2SigmaM+C*cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)));
		}
		if (iterLimit==0) return Double.NaN;  // formula failed to converge

		final double uSq = cosSqAlpha * (a*a - b*b) / (b*b);
		final double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
		final double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
		final double deltaSigma = B*sinSigma*(cos2SigmaM+B/4*(cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)-B/6*cos2SigmaM*(-3+4*sinSigma*sinSigma)*(-3+4*cos2SigmaM*cos2SigmaM)));	
		final double s = b*A*(sigma-deltaSigma);

		// initial & final bearings
		//double fwdAz = atan2(cosU2*sinLambda, cosU1*sinU2-sinU1*cosU2*cosLambda);
		//double revAz = atan2(cosU1*sinLambda, -sinU1*cosU2+cosU1*sinU2*cosLambda);

		return toFixed(s, 3); // round to 1mm precision
	}
	
	private double toFixed(double value , int decimals){
		double d = pow (10, decimals);
		return (ceil(value * d))/d;
	}


}
