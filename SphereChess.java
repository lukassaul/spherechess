import idx3d.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.net.URL;

/**
*  This is just an Object for providing handles for things we've built
*
*  Try to keep GUI and construction techniques elsewhere
*
*  This is the superclass that contains the game
*/
public class SphereChess {

	//private Vector pieces; //don't need this anymore
	private Hashtable squares; // th3 index here is not the square number!!
	private ChessBoardFactory cbf;
	private ChessPieceFactory cpf;
	private URL docBase;

	/**
	*  Use this for applcations
	*/
	public SphereChess() {
		this(null);
	}

	/**
	* and this for from an applet
	*/
	public SphereChess(URL doc) {
		docBase = doc;

		// ok, get the teams to work.
		cbf = new ChessBoardFactory(docBase);
		cpf = new ChessPieceFactory(docBase);

		squares = new Hashtable(72);
		cbf.addBlankSquares(squares);

		cpf.addStartingPieces(squares);

		// well that's basically most of the work...
		System.out.println("Done SphereCHess constructor");
	}

//	public Square getSquare(String num) {return (Square)squares.get(num);}
//	public Piece getPiece(int j) {return (Piece)pieces.elementAt(j);}
//	public int getNumberOfPieces() {return pieces.size();}

	/*
	*  OK, this is for the GUI who comes looking for something to display
	*   Redraw everything.
	*/
	public void buildAll(idx3d_Scene scene) {
		System.out.println("about to add everything to the scene");
		try {
			scene.environment.setBackground(
							idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x999999));
			//scene.addLight("Light1",new idx3d_Light(new idx3d_Vector(0.2f,0.2f,1f),0xFFFFFF,320,80));
			//scene.addLight("Light2",new idx3d_Light(new idx3d_Vector(-1f,-1f,1f),0xFFCC99,100,40));
			scene.addLight("Light3",new idx3d_Light(new idx3d_Vector(5f,5f,5f),0xFFFFFF,320,80));
			scene.addLight("Light4",new idx3d_Light(new idx3d_Vector(-5f,-5f,5f),0xFFFFFF,320,80));
			scene.addLight("Light5",new idx3d_Light(new idx3d_Vector(-5f,5f,-5f),0xFFFFFF,320,80));
			scene.addLight("Light6",new idx3d_Light(new idx3d_Vector(-5f,-5f,-5f),0xFFFFFF,320,80));


			// draw the board, and that's it!
			System.out.println("adding squares: " + squares.size());
			Enumeration e = squares.elements();
			int i=0;
			while (e.hasMoreElements()) {
				Square sq = (Square)e.nextElement();
				scene.addObject("Square_"+i,sq.getObject());
				scene.addMaterial("Material_"+i,sq.getMaterial());
				scene.object("Square_"+i).setMaterial(scene.material("Material_"+i));
				idx3d_TextureProjector.projectFrontal(scene.object("Square_"+i));

				if (sq.getOccupied()) {
					scene.addObject("Piece_"+i,sq.getPiece().getObject());
					scene.addMaterial("PMat_"+i,sq.getPiece().getMaterial());
					scene.object("Piece_"+i).setMaterial(scene.material("PMat_"+i));
					idx3d_TextureProjector.projectFrontal(scene.object("Piece_"+i));
				}
				i++;
			}
			System.out.println("added squares: " + i);

			scene.normalize();
		}
		catch (Exception e) {e.printStackTrace();}
	}
}