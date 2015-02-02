package org.opentools.java3d;

import java.io.*;
import java.net.*;
import com.sun.j3d.loaders.*;


/**
 * An implementation of the Loader interface for 3D Studio files.
 * This is currently a very partial implementation.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class ThreeDSLoader extends LoaderBase implements ThreeDSConstants {
    private boolean forceTwoSided = false;
    private boolean forceReverseNormals = false;
    private boolean verbose = false;
    private boolean allowDefaultLighting = false;

    /**
     * Initializes the loader to load everything it can.
     */
    public ThreeDSLoader() {
        super(Loader.LOAD_ALL);
    }

    /**
     * Initializes the loader with the specified flags defining what type
     * of objects to load.  The available flags are defined in the Loader
     * class.
     *
     * @see com.sun.j3d.loaders.Loader
     */
    public ThreeDSLoader(int flags) {
        super(flags);
    }

    /**
     * Gets whether the loader should add default lighting if none is
     * present in the source file.  This flag is ignored if the file has
     * lights and the lighting is loaded from the file.
     */
    public boolean isDefaultLightingAllowed() {
        return allowDefaultLighting;
    }

    /**
     * Sets whether the loader should add default lighting if none is
     * present in the source file.  This flag is ignored if the file has
     * lights and the lighting is loaded from the file.
     */
    public void setDefaultLightingAllowed(boolean allowed) {
        allowDefaultLighting = allowed;
    }

    /**
     * Gets whether the loader writes extra info to standard output as it
     * loads the file.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets whether the loader writes extra info to standard output as it
     * loads the file.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Gets whether the loader will load all faces as two sided regardless
     * of what the file indicates.  This can be used to assure proper
     * display if some or all normals are reversed.
     */
    public boolean isForceTwoSided() {
        return forceTwoSided;
    }

    /**
     * Sets whether the loader will load all faces as two sided regardless
     * of what the file indicates.  This can be used to assure proper
     * display if some or all normals are reversed.
     */
    public void setForceTwoSided(boolean twoSided) {
        forceTwoSided = twoSided;
    }

    /**
     * Gets whether the loader will reverse all normals it calculates.  This
     * can be used if all the normals are originally loaded backwards.
     */
    public boolean isForceReverseNormals() {
        return forceReverseNormals;
    }

    /**
     * Sets whether the loader will reverse all normals it calculates.  This
     * can be used if all the normals are originally loaded backwards.
     */
    public void setForceReverseNormals(boolean reverse) {
        forceReverseNormals = reverse;
    }

    public Scene load(String fileName) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
        Scene scene = ThreeDSProcessor.load(in, this);
        try {
            in.close();
        } catch(IOException e) {}
        return scene;
    }

    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        try {
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            Scene scene = ThreeDSProcessor.load(in, this);
            in.close();
            return scene;
        } catch(IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public Scene load(Reader reader) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        throw new ParsingErrorException("3DS Files are not a character based format.  Use load(URL) or load(fileName) instead.");
    }
}