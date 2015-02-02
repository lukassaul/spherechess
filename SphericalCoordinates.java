import java.math.*;


/**
*  This uses theta like Co-Latitude - warning!
*/
public class SphericalCoordinates {
	private double r;

 	public SphericalCoordinates(double _r) {
		r = _r;
	}

	/**
	*  This uses theta like Co-Latitude - warning!
	*/
 	public double[] getCartesian(double phi, double theta) {
		double[] tbr = new double[3];
		tbr[0] = r*Math.cos(theta)*Math.cos(phi);
		tbr[1] = r*Math.cos(theta)*Math.sin(phi);
		tbr[2] = r*Math.sin(theta);
		return tbr;
	}

	public static double getPhi(double x, double y, double z) {
		// this stuff is because Math.atrig(arg) returns -pi/2<theta<pi/2 :
		// we want 0<phi<2pi
		double phi = 0.0;
		if (x>0) phi = Math.atan(y/x);
		else if (x<0) phi = Math.atan(y/x) + Math.PI;
		else if (x==0 & y>0) phi = Math.PI/2;
		else if (x==0 & y<0) phi = 3*Math.PI/2;
		else if (x==0 & y==0) phi = 0;
		if (phi<0) phi+=Math.PI*2;
		else if (phi>Math.PI*2) phi-=Math.PI*2;
		return phi;
	}

	public static double getTheta(double x, double y, double z) {
		double theta = 0.0;
		if (x==0 & y==0 & z==0) { return 0.0; }
		else {
			// we want theta>=0 & theta<= PI
			theta = Math.acos(z/Math.sqrt(x*x+y*y+z*z));
			return theta;
		}
	}


}
