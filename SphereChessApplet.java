import idx3d.*;
import java.awt.*;
import java.applet.*;

public final class SphereChessApplet extends Applet implements Runnable
{
	private Thread idx_Thread;
	idx3d_Scene scene;

	int oldx=0;
	int oldy=0;
	boolean autorotation=true;
	boolean antialias=false;


	public void init()
	{
		setNormalCursor();

		// BUILD SCENE
		idx3d_Texture t = new idx3d_Texture(getDocumentBase(),"chrome.jpg");

		scene=new idx3d_Scene(this.size().width,this.size().height);scene.environment.setBackground(
				idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x999999));

		//scene.addLight("Light1",new idx3d_Light(new idx3d_Vector(0.2f,0.2f,1f),0xFFFFFF,320,80));
		//scene.addLight("Light2",new idx3d_Light(new idx3d_Vector(-1f,-1f,1f),0xFFCC99,100,40));
		scene.addLight("Light3",new idx3d_Light(new idx3d_Vector(5f,5f,5f),0xFFFFFF,320,80));
		scene.addLight("Light4",new idx3d_Light(new idx3d_Vector(-5f,-5f,5f),0xFFFFFF,320,80));
		scene.addLight("Light5",new idx3d_Light(new idx3d_Vector(-5f,5f,-5f),0xFFFFFF,320,80));
		scene.addLight("Light6",new idx3d_Light(new idx3d_Vector(-5f,-5f,-5f),0xFFFFFF,320,80));

		SphereChess sc = new SphereChess(getDocumentBase());
		sc.buildAll(scene);

	}

	public synchronized void paint(Graphics g)
	{
	}

	public void start()
	{
		if (idx_Thread == null)
		{
			idx_Thread = new Thread(this);
			idx_Thread.start();
		}
	}

	public void stop()
	{
		if (idx_Thread != null)
		{
			idx_Thread.stop();
			idx_Thread = null;
		}
	}

	public void run()
	{
		while(true)
		{
			repaint();
			try
			{
				idx_Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				System.out.println("idx://interrupted");
			}
		}
	}

	public synchronized void update(Graphics g)
	{
		if (autorotation)
		{
			//scene.object("Torus").rotate(0f,0.05f,0.03f);
			//scene.object("Wineglass").rotate(0f,-0.08f,-0.1f);
		}
		scene.render();
		g.drawImage(scene.getImage(),0,0,this);
	}

	public boolean imageUpdate(Image image, int a, int b, int c, int d, int e)
   	{
		return true;
   	}

	public boolean mouseDown(Event evt,int x,int y)
	{
		oldx=x;
		oldy=y;
		setMovingCursor();
		return true;
	}

	public boolean keyDown(Event evt,int key)
	{
		if (key==32) { System.out.println(scene.getFPS()+""); return true; }
		if (key==Event.PGUP) {scene.defaultCamera.shift(0f,0f,0.2f); return true; }
		if (key==Event.PGDN) {scene.defaultCamera.shift(0f,0f,-0.2f); return true; }
		if (key==Event.UP) {scene.defaultCamera.shift(0f,-0.2f,0f); return true; }
		if (key==Event.DOWN) {scene.defaultCamera.shift(0f,0.2f,0f); return true; }
		if (key==Event.LEFT) {scene.defaultCamera.shift(0.2f,0f,0f); return true; }
		if (key==Event.RIGHT) {scene.defaultCamera.shift(-0.2f,0f,0f); return true; }
		if ((char)key=='+') {scene.scale(1.2f); return true; }
		if ((char)key=='-') {scene.scale(0.8f); return true; }
		if ((char)key=='a') {antialias=!antialias; scene.setAntialias(antialias); return true; }
		if ((char)key=='m') {for (int i=0;i<scene.objects;i++) scene.object[i].meshSmooth(); return true; }

		if ((char)key=='i') {idx3d.debug.Inspector.inspect(scene); return true; }

		return true;
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

	public boolean mouseUp(Event evt,int x,int y)
	{
		autorotation=true;
		setNormalCursor();

		return true;
	}

	private void setMovingCursor()
	{
		if (getFrame()==null) return;
		getFrame().setCursor(Frame.MOVE_CURSOR);
	}

	private void setNormalCursor()
	{
		if (getFrame()==null) return;
		getFrame().setCursor(Frame.HAND_CURSOR);
	}

	private Frame getFrame()
	{
		Component comp=this;
		while ((comp=comp.getParent())!=null) if(comp instanceof Frame) return (Frame)comp;
		return null;
	}
}
