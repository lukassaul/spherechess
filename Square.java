import idx3d.idx3d_Object;
import idx3d.idx3d_Material;
import idx3d.idx3d_Vector;
import java.net.URL;
//import idx3d.idx3d_Math;

/**
*  This is just an Object for providing handles for things we've built
*
*  Try to keep GUI and construction techniques elsewhere
*/
public class Square {
	private Piece piece;         // a piece occupying the square
	private idx3d_Object object; // the 3d object that makes the square
	private idx3d_Material material; // the 3d material that covers the square
	private String number;
	private static float PI = (float)Math.PI;

	public Square() {
		piece = null;
		object = null;
		material = null;
		number = "";
	}

	public void setObject(idx3d_Object obj) {object=obj;}
	public void setMaterial(idx3d_Material obj) {material=obj;}
	public void setNumber(String k) {number=k;}

	public String getNumber() {return number;}
	public boolean getOccupied() { return !(piece==null); }
	public Piece getPiece() {return piece;}
	public idx3d_Object getObject() {return object;}
	public idx3d_Material getMaterial() {return material;}

	// well these are pretty much useless I guess with getObject...
	//public idx3d_Vector getVector() {return object.getPos();}
	//public idx3d_Vector getMin() {return object.min();}
	//public idx3d_Vector getMax() {return object.max();}
	//public idx3d_Vector getCenter() {return object.getCenter();}

	/**
	* use this to move the piece to proper orientation and position as well
	*
	*  This is probably where moving piece code would go??
	*
	*
	*
	*/
	public void setPiece(ChessPieceFactory cpf, Piece p) {
		piece=p;
		object.rebuild();
		//System.out.println(number + " " +object.max()+" max");
		//System.out.println(object.min()+" min");
		//System.out.println(object.getPos()+" pos");
		System.out.println("center: "+object.getCenter());
		//System.out.println("angle test rad: " + idx3d_Math.cos(PI/2) + " deg: "
		//				+ idx3d_Math.cos(90.0f));

		//piece.reset();
		piece.setObject(cpf.getPiece(piece.getType()));
		piece.getObject().rebuild();
		//piece.getObject().scale(.0002f);
		//p.getObject().detach(); // that should set the position at the center for rotating


		// ok, lets find the angles to the square.  This kind of sucks
		idx3d_Vector center = object.getCenter();
		//idx3d_Vector pieceTop = piece.getObject().max();
		//System.out.println("piece top: " + pieceTop);

		piece.getObject().rotateSelf(getRotationVector(center));//.normalize().rotate(1.0f,0.0f,1.0f));
		//p.getObject().rebuild();
		piece.getObject().setPos(object.getCenter().reverse());
		// just for now add another to compare
	}

	/**
	* Find rotation vector for piece, initially with top to + z axis, to a target vector
	*
	*  It looks like idx3d takes a rotation vector in radians, about each axis?
	*
	*/
	private idx3d_Vector getRotationVector(idx3d_Vector target) {
		// ok, lets try to get one of these bitches
		float xa=0.0f; float ya=0.0f; float za = 0.0f;

		float phi = (float)SphericalCoordinates.getPhi((double)target.x,
						(double)target.y, (double)target.z);

		float theta = (float)SphericalCoordinates.getTheta((double)target.x,
						(double)target.y, (double)target.z);

		if (target.x>0) theta = PI - theta;

	//	xa = (float)Math.acos(idx3d_Vector.angle(new idx3d_Vector(0.0f, target.y, target.z),
	//								new idx3d_Vector(0.0f, 0.0f, 1.0f)));

	//	ya = (float)Math.acos(idx3d_Vector.angle(new idx3d_Vector(target.x, 0.0f, target.z),
	//								new idx3d_Vector(0.0f, 0.0f, 1.0f)));

	//	za = (float)Math.acos(idx3d_Vector.angle(new idx3d_Vector(target.x, target.y, 0.0f),
	//								new idx3d_Vector(0.0f, 0.0f, 0.0f)));

		// I may have to do this bullshit on my own...
	//	xa = -PI/4;

		System.out.println("xa, ya, za: " + xa + " "+ya+" "+za);
		return new idx3d_Vector(phi,theta,za).reverse();
	}
}