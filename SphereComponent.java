import idx3d.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Vector;
import java.net.URL;
//import java.applet.Applet;
/**
*
*  Getting this stuff from idx of course..
*
*/
public class 3DComponent extends Panel implements Runnable {
	private Thread idx_Thread;
	idx3d_Scene scene;
	boolean initialized=false;
	boolean antialias=false;

	int oldx=0;
	int oldy=0;
	boolean autorotation=false;

	public SphereComponent()	{ }

	public void init()	{
		setNormalCursor();

		// BUILD SCENE
	//	buildChessboard();
		scene=new idx3d_Scene(this.size().width,this.size().height);

		//idx3d_Material crystal=new idx3d_Material("glass.material");
		//crystal.setReflectivity(255);
		//scene.addMaterial("Crystal",crystal);

		//idx3d_Material plastic=new idx3d_Material(new idx3d_Texture("texture.jpg"));
		//scene.addMaterial("White",plastic);

		//idx3d_Material check=new idx3d_Material(
		//		idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x999999));
		//scene.addMaterial("Black",check);

	//	System.out.println("added materials...");
		//scene.environment.setBackground(
		//		idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x999999));

		//scene.addLight("Light1",new idx3d_Light(new idx3d_Vector(0.2f,0.2f,1f),0xFFFFFF,320,80));
		//scene.addLight("Light2",new idx3d_Light(new idx3d_Vector(-1f,-1f,1f),0xFFCC99,100,40));
		//scene.addLight("Light3",new idx3d_Light(new idx3d_Vector(5f,5f,5f),0xFFFFFF,320,80));
		//scene.addLight("Light4",new idx3d_Light(new idx3d_Vector(-5f,-5f,5f),0xFFFFFF,320,80));
		//scene.addLight("Light5",new idx3d_Light(new idx3d_Vector(-5f,5f,-5f),0xFFFFFF,320,80));
		//scene.addLight("Light6",new idx3d_Light(new idx3d_Vector(-5f,-5f,-5f),0xFFFFFF,320,80));

		//scene.addLight("Light1",new idx3d_Light(new idx3d_Vector(0.2f,0.2f,1f)));
		//scene.addLight("Light2",new idx3d_Light(new idx3d_Vector(-1f,-1f,1f)));
		//scene.addLight("Light3",new idx3d_Light(new idx3d_Vector(5f,5f,5f)));

		/*idx3d_Scene dummy = new idx3d_Scene(0,0);

		try {
			new idx3d_3ds_Importer().importFromStream(new FileInputStream("bishop20.3ds"),dummy);
		}
		catch (Exception e) {e.printStackTrace();}

		idx3d_Object theBishop = dummy.object("object");
		//scene.addObject("object",theBishop);*/

		/*double[] tri1 = {0.0,Math.PI/2.0,
								Math.PI/8.0,Math.PI/2+Math.PI/8,
								0.0, Math.PI/2+Math.PI/8};
		idx3d_Object aTest = getSphereChunk(tri1,3);

		double[] tri2 = {2.0,Math.PI/2.0,
								2.0+Math.PI/8.0,Math.PI/2+Math.PI/8,
								2.0, Math.PI/2+Math.PI/8};
		idx3d_Object aTest2 = getSphereChunk(tri2,3);
		scene.addObject("Chunk",aTest);
		scene.addObject("Chunk2",aTest2);*/

/*		ChessBoardFactory cbf = new ChessBoardFactory();

		cbf.addChessBoard(scene);

		ChessPieceFactory cpf = new ChessPieceFactory();

		cpf.addPieces(scene);
*/
		//scene.addObject("Sphere",idx3d_ObjectFactory.SPHERE(1.0f,60));
		//scene.object("Sphere").scale(0.72f);
		//scene.object("Sphere").shift(-0.5f, 1f, 0f);
		//scene.object("object").scale(0.72f);
		//scene.object("object").shift(-0.5f, 0f, 0f);


		//scene.addObject("Torus",idx3d_ObjectFactory.ROTATIONOBJECT(path,16));
			//scene.object("Torus").rotate(4.2f,0.2f,-0.5f);
			//scene.object("Torus").shift(-0.5f,0f,0f);
			//scene.object("Torus").scale(0.72f);
			//scene.object("Torus").setMaterial(scene.material("Plastic"));

		try {
			String home = System.getProperty("user.dir");
			//URL docBase = new Applet().getDocumentBase();
			//URL docBase = new URL("file://"+home);
			SphereChess sc = new SphereChess();
			sc.buildAll(scene);
		}
		catch (Exception e) {e.printStackTrace();}
		System.out.println("added objects...");

		scene.rebuild();
		//scene.normalize();

		//scene.object("object").setMaterial(scene.material("Plastic"));
		//scene.object("object").setMaterial(scene.material("check"));
		//scene.object("Sphere").setMaterial(scene.material("check"));
		//scene.object("Chunk").setMaterial(scene.material("check"));
		//scene.object("Chunk2").setMaterial(scene.material("check"));
		System.out.println("set materials to objects..");

		idx_Thread = new Thread(this);
		idx_Thread.start();

		initialized=true;
	}



	public synchronized void paint(Graphics g)
	{
		repaint();
	}

	public void run()
	{
		while(true)
		{
			repaint();
			try
			{
				idx_Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				System.out.println("idx://interrupted");
			}
		}
	}

	public synchronized void update(Graphics g)	{
		if (!initialized) return;
		if (autorotation)		{
			scene.object("Torus").rotate(-0.05f,0f,0.05f);
			scene.object("Wineglass").rotate(0.00f,0.08f,-0.05f);
		}
		scene.render();
		g.drawImage(scene.getImage(),0,0,this);
	}

	public boolean imageUpdate(Image image, int a, int b, int c, int d, int e)	{
   	     return true;
   	}

	public boolean mouseDown(Event evt,int x,int y)	{
		oldx=x;
		oldy=y;

		autorotation=false;
		System.out.println("Trying to identify object...");
		idx3d_Object obj=scene.identifyObjectAt(oldx,oldy);
		if (obj!=null)
		{
			System.out.println(obj.name);
			//if (obj.name.equals("Link1")) getAppletContext().showDocument(link1,"_blank");
			//if (obj.name.equals("Link2")) getAppletContext().showDocument(link2,"_blank");
			//if (obj.name.equals("Link3")) getAppletContext().showDocument(link3,"_blank");
		}


		setMovingCursor();
		return true;
	}

	public boolean keyDown(Event evt,int key)	{
		if (key==32) { System.out.println(scene.getFPS()+""); return true; }
		if (key==Event.PGUP) {scene.defaultCamera.shift(0f,0f,0.2f); return true; }
		if (key==Event.PGDN) {scene.defaultCamera.shift(0f,0f,-0.2f); return true; }
		if (key==Event.UP) {scene.defaultCamera.shift(0f,0.2f,0f); return true; }
		if (key==Event.DOWN) {scene.defaultCamera.shift(0f,-0.2f,0f); return true; }
		if (key==Event.LEFT) {scene.defaultCamera.shift(-0.2f,0f,0f); return true; }
		if (key==Event.RIGHT) {scene.defaultCamera.shift(0.2f,0f,0f); return true; }
		if ((char)key=='+') {scene.scale(1.2f); return true; }
		if ((char)key=='-') {scene.scale(0.8f); return true; }
		if ((char)key=='.') {scene.defaultCamera.roll(0.2f); return true; }
		if ((char)key==',') {scene.defaultCamera.roll(-0.2f); return true; }
		if ((char)key=='a') {antialias=!antialias; scene.setAntialias(antialias); return true; }

		if ((char)key=='e') {export(); return true; }
		if ((char)key=='i') {idx3d.debug.Inspector.inspect(scene); return true; }

		return true;
	}

	private void export()
	{
		try{
			idx3d_3ds_Exporter.exportToStream(new java.io.FileOutputStream(new java.io.File("export.3ds")),scene);
		}
		catch(Exception ignored){}
	}

	public boolean mouseDrag(Event evt,int x,int y)
	{
		autorotation=false;
		float dx=(float)(y-oldy)/50;
		float dy=(float)(oldx-x)/50;
		scene.rotate(dx,dy,0);
		oldx=x;
		oldy=y;
		return true;
	}

	public boolean mouseUp(Event evt,int x,int y)	{
		//autorotation=true;
		setNormalCursor();
		return true;
	}

	private void setMovingCursor()	{
		if (getFrame()==null) return;
		getFrame().setCursor(Frame.MOVE_CURSOR);
	}

	private void setNormalCursor()	{
		if (getFrame()==null) return;
		getFrame().setCursor(Frame.HAND_CURSOR);
	}

	private Frame getFrame()	{
		Component comp=this;
		while ((comp=comp.getParent())!=null) if(comp instanceof Frame) return (Frame)comp;
		return null;
	}

	public void reshape(int x, int y, int w, int h)	{
		super.reshape(x,y,w,h);
		if (!initialized) init();
		scene.resize(w,h);
	}


	public synchronized void repaint()	{
		if (getGraphics() != null) update(getGraphics());
	}
}
