import idx3d.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Vector;
import java.net.URL;
//import java.applet.Applet;

/**
*  This is a panel, does the 3d display business for us
*
*/
public class SphereChessComponent extends Panel implements Runnable {
	private Thread idx_Thread;
	idx3d_Scene scene;
	boolean initialized=false;
	boolean antialias=false;

	int oldx=0;
	int oldy=0;
	boolean autorotation=false;
	private SphereChess theMain;

	public SphereChessComponent(SphereChess sc)	{
		// pointers rock
		theMain = sc;
	}

	public void init()	{
		setNormalCursor();

		//resize(300,200);

		// BUILD SCENE
	//	buildChessboard();
		scene=new idx3d_Scene(300,200);

		theMain.buildAll(scene);
		scene.rebuild();


		System.out.println("set materials to objects..");

		idx_Thread = new Thread(this);
		idx_Thread.start();

		initialized=true;
	}



	public synchronized void paint(Graphics g) {
		repaint();
	}


	public void run() {
		while(true)	{
			repaint();
			try	{
				idx_Thread.sleep(10);
			}
			catch (InterruptedException e)	{
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

	private void export()	{
		try{
			idx3d_3ds_Exporter.exportToStream(new java.io.FileOutputStream(new java.io.File("export.3ds")),scene);
		}
		catch(Exception ignored){}
	}

	public boolean mouseDrag(Event evt,int x,int y)	{
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
