import idx3d.idx3d_Object;
import idx3d.idx3d_Material;

/**
*  This is just an Object for providing handles for things we've built
*   Basically a storage convenience
*  Try to keep GUI and construction techniques elsewhere
*/
public class Piece {
	private String type;         // i.e. "bishop", "knight", "rook", etc.

	// don't worry, this is just a pointer
	private idx3d_Object object; // the 3d object that shows up

	//private idx3d_Object originalObject; // the non-rotated, non-shifted guy
	private idx3d_Material material; // the 3d material that makes the piece

	private boolean color;       // white = true;
	private Square location;
	public Piece() {
		type = "";
		object = null;
		color = true;
		location = null;
	}

	public void setType(String s) {type=s;}
	public void setObject(idx3d_Object obj) {object = obj;}// originalObject = obj.getClone();}
	public void setMaterial(idx3d_Material obj) {material = obj;}
	public void setColor(boolean b) {color=b;}
	public void setLocation(Square s) {location = s;}

	public String getType() {return type;}
	public idx3d_Object getObject() {return object;}
	//public void reset() {object = originalObject.getClone();}
	public idx3d_Material getMaterial() {return material;}
	public boolean getColor() {return color;}
	public Square getLocation() {return location;}

}


