import idx3d.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.Hashtable;
import java.net.URL;

/**
*  Use this class to build the pieces
*
*  Each "piece" is going to be a 3dObject, contained in Piece object in Square object.
*/
public class ChessPieceFactory {
	public float sf = .0002f;
	private URL docBase;
	//private idx3d_Object bishop, rook, king, queen, knight, pawn; //raw pieces;

	/**
	*   Not in an applet?
	*/
	public ChessPieceFactory() {
		this(null);
	}

	/**
	* Or else do instantiate..
	*/
	public ChessPieceFactory(URL doc) {
		docBase = doc;
	}

	/**
	*  Load the pieces from the 3ds files we converted from GEO..
	*  Somebody is the man here
	*/
	public idx3d_Object getPiece(String key) {
		try {
			idx3d_Object tbr=null;
			idx3d_Scene dummy = new idx3d_Scene(0,0);
			idx3d_3ds_Importer imp = new idx3d_3ds_Importer();
			if (key.equals("king")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("king20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase,"king20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			else if (key.equals("queen")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("queen20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase, "queen20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			else if (key.equals("rook")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("rook20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase, "rook20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			else if (key.equals("bishop")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("bishop20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase,"bishop20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			else if (key.equals("knight")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("knight20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase,"knight20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			else if (key.equals("pawn")) {
				if (docBase==null) imp.importFromStream(new FileInputStream("pawn20.3ds"),dummy);
				else imp.importFromURL(new URL(docBase, "pawn20.3ds"),dummy);
				tbr = dummy.object("object");
				tbr.removeDuplicateVertices();
				tbr.scale(sf);
				dummy.removeAllObjects();
			}
			return tbr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}





	/**
	* Use this to put an initial configuration into a Hashtable of squares
	*/
	public void addStartingPieces(Hashtable squares) {
		idx3d_Material white, black;
		if (docBase == null) {
			white=new idx3d_Material(new idx3d_Texture("white.jpg"));
			black=new idx3d_Material(new idx3d_Texture("black.jpg"));
		}
		else  {
			white=new idx3d_Material(new idx3d_Texture(docBase, "white.jpg"));
			black=new idx3d_Material(new idx3d_Texture(docBase, "black.jpg"));
		}

		// OK, construct starting piece set
		String hemis = "N";
		idx3d_Material side = white;
		Square s;  Piece p;
		for (int i=0; i<3; i++) {
			System.out.println("adding pieces to: " + hemis);

			s = (Square)squares.get("1"+hemis);
			p = new Piece();
			if (i==0) {
				p.setType("queen");
			}
			else {
				p.setType("king");
			}
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("2"+hemis);
			p = new Piece();
			if (i==0) {
				p.setType("king");
			}
			else {
				p.setType("queen");
			}
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("3"+hemis);
			p = new Piece();
			p.setType("rook");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("4"+hemis);
			p = new Piece();
			p.setType("bishop");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("5"+hemis);
			p = new Piece();
			p.setType("knight");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("6"+hemis);
			p = new Piece();
			p.setType("rook");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("7"+hemis);
			p = new Piece();
			p.setType("bishop");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("8"+hemis);
			p = new Piece();
			p.setType("knight");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("10"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("11"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("12"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("13"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("15"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("16"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("17"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			s = (Square)squares.get("18"+hemis);
			p = new Piece();
			p.setType("pawn");
			p.setMaterial(side);
			s.setPiece(this,p);

			side = black;
			hemis = "S";
		}
	}
}



