package org.opentools.java3d;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import com.sun.j3d.loaders.SceneBase;

/**
 * The 3DS implementation of Scene.  Adds some hooks and extra information
 * to the basic Scene interface.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class ThreeDSScene extends SceneBase {
    private Point3f min;
    private Point3f max;
    private float scale;
    private Bounds bounds;
    private TransformGroup objectParent;

    ThreeDSScene() {
    }

    void setScale(Point3f min, Point3f max, float scale) {
        this.min = min;
        this.max = max;
        this.scale = scale;
    }

    void setObjectParent(TransformGroup tg) {
        objectParent = tg;
    }

    /**
     * Gets a TransformGroup that's between the lighting and the objects.
     * This lets you manipulate the objects without altering the lighting.
     */
    public TransformGroup getObjectParent() {
        return objectParent;
    }

    void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    /**
     * Gets the Bounds used for lighting.  This extends somewhat farther
     * than the loaded objects in all directions.
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Gets the minimum corner of the bounding box for the model.
     */
    public Point3f getBoundsMin() {
        return min;
    }

    /**
     * Gets the maximum corner of the bounding box for the model.
     */
    public Point3f getBoundsMax() {
        return max;
    }

    /**
     * Gets the length of the largest dimension of the bounding box for the
     * model.  This does not actually measure the longest ray from model
     * point to model point, but is a useful indication of the overall scale
     * of the model.
     */
    public float getScale() {
        return scale;
    }
}