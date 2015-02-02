import idx3d.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.Hashtable;
import java.net.URL;

/**
*  Use this class to build the board itself - the 3dObjects that is...
*
*  Each "square" is going to be a 3dObject
*/
public class ChessBoardFactory {

	private idx3d_Object[] squareObjects;
	private idx3d_Material[] squareMaterials;
	private String[] number;
	private static double PI = Math.PI;
	SphericalCoordinates sc;
	private URL docBase;

	/**
	*  Here we can pass in parameters such as which configuration we want...
	*/
	public ChessBoardFactory() {
		this (null);
	}

	/**
	* Haven't gotten the extra segments to work..
	*  let's stick with an imperfect sphere for now.
	*
	*/
	public ChessBoardFactory(URL doc) {
		docBase = doc;
		int s = 0;
		sc = new SphericalCoordinates(1.0);
		//double[] v;
		// OK, lets just do it by hand
		squareObjects = new idx3d_Object[72];
		number = new String[72];

		//we are going to build 8 the board in 8 groups of 9 for this implementation
		double ps = 0.0;  // phi start
		double h = +1;   // hemisphere

		for (int i=0; i<8; i++) {

			// central triangular square
			double[] v0 = {-PI/20 + ps, 0.0,   PI/20 + ps, 0.0,   ps, h*PI/10};
			squareObjects[9*i+0] = getSphereChunk(v0,s);

			// moving left
			double[] v1 = {-3*PI/20 + ps,0.0,  -PI/20 + ps,0.0,  ps, h*PI/10,  ps-PI/8, h*2*PI/12};
			squareObjects[9*i+1] = getSphereChunk(v1,s);

			// far left bottom square
			double[] v3 = {ps-PI/4,0.0, -3*PI/20 + ps,0.0,  ps-PI/8, h*2*PI/12,  ps-PI/4, h*PI/6};
			squareObjects[9*i+2] = getSphereChunk(v3,s);

			// now just right of tri
			double[] v2 = {3*PI/20 + ps,0.0,  PI/20 + ps,0.0,  ps, PI/10*h,  ps+PI/8, h*2*PI/12};
			squareObjects[9*i+3] = getSphereChunk(v2,s);

			// far right bottom
			double[] v4 = {ps+PI/4,0.0, 3*PI/20 + ps,0.0,  ps+PI/8, h*2*PI/12,  ps+PI/4, h*PI/6};
			squareObjects[9*i+4] = getSphereChunk(v4,s);

			// middle right
			double[] v6 = {ps+PI/8,h*2*PI/12,  ps + PI/4,h*PI/6,  ps+PI/4, h*PI/3,  ps, h*3*PI/10};
			squareObjects[9*i+5] = getSphereChunk(v6,s);

			//middle
			double[] v5 = {ps,h*PI/10, ps + PI/8,h*2*PI/12,  ps, h*3*PI/10,  ps - PI/8,h*2*PI/12};
			squareObjects[9*i+6] = getSphereChunk(v5,s);

			// middle left
			double[] v7 = {ps-PI/8,h*2*PI/12,  ps - PI/4,h*PI/6,  ps-PI/4, h*PI/3,  ps, h*3*PI/10};
			squareObjects[9*i+7] = getSphereChunk(v7,s);

			// top!
			double[] v8 = {ps,h*3*PI/10,   ps + PI/4,h*PI/3,  ps+PI/4, h*PI/2,  ps - PI/4, h*PI/3};
			squareObjects[9*i+8] = getSphereChunk(v8,s);

			// get ready for next run through..
			if (i!=7) {
				if (h==1) h=-1;
				else {
					ps+=PI/2;
					h=1;
				}
			}
		}
		System.out.println("Constructed idx3d objects in ChessBoardFactory constructor");
	}

	/*
	*  Here's what the boss wants us sweatshop boys to produce
	*/
	public void addBlankSquares(Hashtable h) {
		System.out.println("starting getBlankSquares()");
		//Square[] tbr = new Square[72]; // index here is not the square number!!

		// set up materials - we need to be ready for applet action here as well...
		idx3d_Material whiteMat, blackMat;
		if (docBase==null) {
			whiteMat = new idx3d_Material(new idx3d_Texture("3N.jpg"));
			blackMat = new idx3d_Material(new idx3d_Texture("2N.jpg"));
		}
		else {
			whiteMat = new idx3d_Material(new idx3d_Texture(docBase, "2N.jpg"));
			blackMat = new idx3d_Material(new idx3d_Texture(docBase, "3N.jpg"));
		}

		boolean white = true;

		// OK, lets build it
		for (int i=0; i<72; i++) {
			//System.out.println("building : " + i);
			Square s=new Square();
			s.setObject(squareObjects[i]);
			//System.out.println("set object: " + i);
			s.setNumber(map(i));
			//System.out.println("set number: " + i);
			if (i==18 | i==36 | i==54) white = !white;

			if (white) s.setMaterial(whiteMat);
			else s.setMaterial(blackMat);
			white = !white;
			System.out.println("about to put: " + s.getNumber() + " -" + i);
			h.put(s.getNumber(),s);
		}
		System.out.println("added blank squares: " + h.size());
	}

	/**
	* a bit of a hack here.. translate from the index used in construction to square "number"
	*/
	public String map(int i) {
		i++; // whoops!!  (heh heh - much easier than changing every number below)
		switch (i) {
			case 1 : {return "2N";}
			case 2 : {return "5N";}
			case 3 : {return "12N";}
			case 4 : {return "7N";}
			case 5 : {return "16N";}
			case 6 : {return "15N";}
			case 7 : {return "6N";}
			case 8 : {return "13N";}
			case 9 : {return "14N";}
			case 10 : {return "1N";}
			case 11 : {return "4N";}
			case 12 : {return "11N";}
			case 13 : {return "8N";}
			case 14 : {return "17N";}
			case 15 : {return "18N";}
			case 16 : {return "3N";}
			case 17 : {return "10N";}
			case 18 : {return "9N";}

			case 19 : {return "1E";}
			case 20 : {return "29N";}
			case 21 : {return "24N";}
			case 22 : {return "28S";}
			case 23 : {return "21S";}
			case 24 : {return "22S";}
			case 25 : {return "2E";}
			case 26 : {return "23N";}
			case 27 : {return "3E";}
			case 28 : {return "12E";}
			case 29 : {return "30N";}
			case 30 : {return "25N";}
			case 31 : {return "27S";}
			case 32 : {return "20S";}
			case 33 : {return "19S";}
			case 34 : {return "11E";}
			case 35 : {return "26N";}
			case 36 : {return "10E";}

			case 37 : {return "2S";}
			case 38 : {return "5S";}
			case 39 : {return "12S";}
			case 40 : {return "7S";}
			case 41 : {return "16S";}
			case 42 : {return "15S";}
			case 43 : {return "6S";}
			case 44 : {return "13S";}
			case 45 : {return "14S";}
			case 46 : {return "1S";}
			case 47 : {return "4S";}
			case 48 : {return "11S";}
			case 49 : {return "8S";}
			case 50 : {return "17S";}
			case 51 : {return "18S";}
			case 52 : {return "3S";}
			case 53 : {return "10S";}
			case 54 : {return "9S";}

			case 55 : {return "6E";}
			case 56 : {return "29S";}
			case 57 : {return "24S";}
			case 58 : {return "28N";}
			case 59 : {return "21N";}
			case 60 : {return "22N";}
			case 61 : {return "5E";}
			case 62 : {return "23S";}
			case 63 : {return "4E";}
			case 64 : {return "7E";}
			case 65 : {return "30S";}
			case 66 : {return "25S";}
			case 67 : {return "27N";}
			case 68 : {return "20N";}
			case 69 : {return "19N";}
			case 70 : {return "8E";}
			case 71 : {return "26S";}
			case 72 : {return "9E";}
		}
		return null;
	}

	/**
	*  Build a sector of a sphere between the vertices.
	*  I guess the vertices had better all be on the sphere...
	*c:
	*  so we'll make them 2d - azimuth and elevation (phi, theta)
	*/
	private idx3d_Object getSphereChunk(double[] v, int segments) {
		Vector vct = new Vector();
		// two cases, square or triangle..
		boolean square = false;
		boolean triangle = false;
		if (v.length == 6) triangle = true;
		if (v.length == 8) square = true;

		if (triangle) {
			// get the vertices...
			addVerticesInTriangle(vct,v,segments);
		}
		if (square) { // make triangles than get vertices
			double[] t1 = new double[6];
			double[] t2 = new double[6];
			t1[0]=v[0]; t1[1]=v[1];
			t1[2]=v[2]; t1[3]=v[3];
			t1[4]=v[6]; t1[5]=v[7];

			t2[0]=v[2]; t2[1]=v[3];
			t2[2]=v[4]; t2[3]=v[5];
			t2[4]=v[6]; t2[5]=v[7];
			addVerticesInTriangle(vct,t1,segments);
			addVerticesInTriangle(vct,t2,segments);
		}

		// ok, time to build the object...
		idx3d_Object n=new idx3d_Object();
		// lets build an array and get the casting out of the way..
		double[][] hp = new double[vct.size()][3];
		for (int i=0; i<hp.length; i++) {
			hp[i]=(double[])vct.elementAt(i);
		}
		int j=hp.length/3;
		//System.out.println("we have hp of length: " + hp.length + " and triangles: " + j);
		for (int i=0; i<hp.length; i++) {
			n.addVertex((float)hp[i][0],(float)hp[i][1],(float)hp[i][2]);
		}
		for (int i=0; i<j; i++) {
			n.addTriangle(i*3,i*3+1,i*3+2);
			n.addTriangle(i*3+2,i*3+1,i*3);
		}
	//	n.meshSmooth();
		return n;
	}

	/*
	*  Return the vertices required to draw this triangle on a sphere
	*   the vertices are returned in a vector of double[]s
	*  recurse to get higher resolution
	*/
	private void addVerticesInTriangle(Vector vct, double[] v, int segments) {
		if (v.length != 6) return;
		// ok, let's get the points
		if (segments==0) {
			double[] h1 = sc.getCartesian(v[0],v[1]);
			double[] h2 = sc.getCartesian(v[2],v[3]);
			double[] h3 = sc.getCartesian(v[4],v[5]);
			vct.addElement(h1); vct.addElement(h2); vct.addElement(h3);
		}
		else { //recurse
			segments--;
			// get midpoints now
			double a1_2p = getMidpoint(v[0],v[2]);
			if (Math.abs(v[1])==PI/2) a1_2p=v[2];
			if (Math.abs(v[3])==PI/2) a1_2p=v[0];
			double a1_3p = getMidpoint(v[0],v[4]);
			if (Math.abs(v[1])==PI/2) a1_3p=v[4];
			if (Math.abs(v[5])==PI/2) a1_3p=v[0];
			double a2_3p = getMidpoint(v[2],v[4]);
			if (Math.abs(v[3])==PI/2) a2_3p=v[4];
			if (Math.abs(v[5])==PI/2) a2_3p=v[2];

			double a1_2t = getMidpoint(v[1],v[3]);
			double a1_3t = getMidpoint(v[1],v[5]);
			double a2_3t = getMidpoint(v[3],v[5]);
			// four more triangles to add now
			double[] tri1 = {v[0],v[1],a1_2p,a1_2t,a1_3p,a1_3t};
			addVerticesInTriangle(vct,tri1,segments);
			double[] tri2 = {v[2],v[3],a1_2p,a1_2t,a2_3p,a2_3t};
			addVerticesInTriangle(vct,tri2,segments);
			double[] tri3 = {v[4],v[5],a1_3p,a1_3t,a2_3p,a2_3t};
			addVerticesInTriangle(vct,tri3,segments);
			double[] tri4 = {a1_2p,a1_2t,a1_3p,a1_3t,a2_3p,a2_3t};
			addVerticesInTriangle(vct,tri4,segments);
			// that wasn't so bad was it
		}
	}

	/*
	*  Return a midpoint between two angle measurements
	*/
	private double getMidpoint(double a, double b) {
		double dif = Math.abs(a-b);
		double dif2 = Math.abs(a+b);
		if (dif>dif2) dif=dif2;
		dif = dif/2;
		if (a<b) return a+dif;
		else return b+dif;
	}
}


/*public void addChessBoard(idx3d_Scene scene) {
		idx3d_Material white=new idx3d_Material(new idx3d_Texture("white.jpg"));
		//idx3d_Material check2=new idx3d_Material(
		//		idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x000000));
		scene.addMaterial("White",white);

		idx3d_Material black=new idx3d_Material(new idx3d_Texture("black.jpg"));
		//idx3d_Material check=new idx3d_Material(
		//		idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x999999,0x999999));
		scene.addMaterial("Black",black);

		boolean w = true;
		for (int i=0; i<squareObjects.length; i++) {
			if (i==18 | i==36 | i==54) w=!w;
			scene.addObject("Square_"+i,squareObjects[i]);
			if (w)	scene.object("Square_"+i).setMaterial(scene.material("White"));
			else scene.object("Square_"+i).setMaterial(scene.material("Black"));
			w = !w;
			idx3d_TextureProjector.projectFrontal(scene.object("Square_"+i));

		//	scene.object("Square_"+i).shift(-0.5f, 1f, 0f);
		//	scene.object("Square_"+i).scale(0.72f);
		}
	}*/
