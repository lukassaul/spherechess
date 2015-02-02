package org.opentools.java3d;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.loaders.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * Sample class to run the 3DS Loader, based on the "GearTest" demo
 * shipped with Java3D.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class LoadTest extends Applet {
    public LoadTest(Scene scene) {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);
        add("Center", c);

        // Create a simple scene and attach it to the virtual universe
        BranchGroup root = new BranchGroup();
        BranchGroup bg = scene.getSceneGroup();
        SimpleUniverse u = new SimpleUniverse(c);

        if(scene instanceof ThreeDSScene) {
            ThreeDSScene tds = (ThreeDSScene)scene;
            TransformGroup scale = new TransformGroup();
            Transform3D t3d = new Transform3D();
            t3d.setScale(1.0/tds.getScale());
            scale.setTransform(t3d);
            root.addChild(scale);
            float ymin = tds.getBoundsMin().y, ymax = tds.getBoundsMax().y;
            float yoffset = (ymin + (ymax-ymin)/2)/tds.getScale();

            TransformGroup transformer = tds.getObjectParent();
            transformer.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            // Create a new Behavior object that will rotate the object and
            // add it into the scene graph.
            Transform3D rot = new Transform3D();
            rot.rotZ((float)Math.PI/4f);
            Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
                                            0, 0,
                                            8000, 0, 0,
                                            0, 0, 0);

            RotationInterpolator rotator = new RotationInterpolator(rotationAlpha,
                                 transformer, rot, (float)-Math.PI, (float) Math.PI);
            rotator.setSchedulingBounds(tds.getBounds());
            transformer.addChild(rotator);

            scale.addChild(bg);

            Transform3D zoffset = new Transform3D();
            zoffset.set(new Vector3d(0, yoffset, 0.7/Math.tan(Math.PI/8.0)));
            u.getViewingPlatform().getViewPlatformTransform().setTransform(zoffset);
        } else {
            root.addChild(bg);

            Transform3D zoffset = new Transform3D();
            zoffset.set(new Vector3d(0, 0, 0.7/Math.tan(Math.PI/8.0)));
            u.getViewingPlatform().getViewPlatformTransform().setTransform(zoffset);
        }
        // Have Java 3D perform optimizations on this scene graph.
        root.compile();

        u.addBranchGraph(root);
    }

    //
    // The following allows LoadTest to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
        Loader loader;
        if(args.length == 1) {
            loader = new ThreeDSLoader();
        } else {
            loader = new ThreeDSLoader(Integer.parseInt(args[1]));
        }
        try {
            Scene scene = loader.load(args[0]);
            ThreeDSScene tds = (ThreeDSScene)scene;
            new MainFrame(new LoadTest(scene), 700, 700);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
