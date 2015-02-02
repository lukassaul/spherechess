import java.awt.*;
import idx3d.*;

public class TestApp extends Frame
{
	public static void main(String[] args)
	{
		new TestApp();
	}

	public TestApp()
	{
		this.resize(320,240);
		this.setTitle("TestApp");
		this.move(200,200);
		this.add(new SphereComponent());
		this.show();
		this.toFront();	
	}
	
	public boolean handleEvent(Event evt)
	{
		if (evt.id==Event.WINDOW_DESTROY)
		{
			System.exit(0);
		}
		return super.handleEvent(evt);
	}
}