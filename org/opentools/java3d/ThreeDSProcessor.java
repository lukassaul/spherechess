package org.opentools.java3d;

import java.io.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.utils.geometry.*;

/**
 * Actually processes a 3DS input file.  Should only be run once (use a
 * different instance for a different file).
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class ThreeDSProcessor implements ThreeDSConstants {
    private final static Color3f DEFAULT_BACKGROUND_COLOR=new Color3f(0.1f, 0.1f, 0.4f);
    private DataInputStream in;
    private ThreeDSScene scene;
    private Exception error;
    private ThreeDSLoader config;
    private int position = 0;
    private Map materials;
    private Color3f solidBackground, ambient;
    private boolean useSolidBackground;
    private float minx, maxx, miny, maxy, minz, maxz;
    private List shapes;
    private Appearance defaultAppearance;
    private boolean showDebugInfo = true;

    /**
     * Creates a 3DS file processor with the specificed source stream
     * and loader configuration.
     */
    public ThreeDSProcessor(InputStream in, ThreeDSLoader config) {
        this.in = new DataInputStream(in);
        this.config = config;
        showDebugInfo = config.isVerbose();
        scene = new ThreeDSScene();
        materials = new HashMap();
        shapes = new ArrayList();

        Material defaultMaterial = new Material();
        defaultMaterial.setEmissiveColor(new Color3f(0.0f,0.0f,0.0f));
        defaultAppearance = new Appearance();
        defaultAppearance.setMaterial(defaultMaterial);
        defaultAppearance.setPolygonAttributes(
            new PolygonAttributes(PolygonAttributes.POLYGON_LINE,
                                  PolygonAttributes.CULL_NONE,
                                  0.0f,
                                  true
            )
        );
    }

    /**
     * Gets the loaded scene.  Only valid after load has been called.
     */
    public ThreeDSScene getScene() {
        return scene;
    }

    /**
     * Convenience method to load a 3DS file.
     */
    public static ThreeDSScene load(InputStream in, ThreeDSLoader config)
                  throws IncorrectFormatException, ParsingErrorException {
        ThreeDSProcessor loader = new ThreeDSProcessor(in, config);
        loader.process();
        return loader.getScene();
    }

    /**
     * After loading, set up the Scene object.  Adds geometry, uses default
     * lighting if none is present in the file, etc.
     */
    protected void createScene() {
        Background bg = new Background(useSolidBackground && solidBackground != null ? solidBackground : DEFAULT_BACKGROUND_COLOR);
        scene.addBackgroundNode(bg);
        BranchGroup root = new BranchGroup();
        float limit = 0;
        if(maxx - minx > limit) {
          limit = maxx - minx;
        }
        if(maxy - miny > limit) {
          limit = maxy - miny;
        }
        if(maxz - minz > limit) {
          limit = maxz - minz;
        }

        BoundingSphere bounds = new BoundingSphere(new Point3d(minx + (maxx-minx)/2,
                                                               miny + (maxy-miny)/2,
                                                               minz + (maxz-minz)/2),
                                                   limit*1.5);
        // Set up the background
        bg.setApplicationBounds(bounds);
        root.addChild(bg);

        // Set up the lights
        Fog[] fogs = scene.getFogNodes();
        if(fogs != null) {
            for(int i=0; i<fogs.length; i++) {
                fogs[i].setInfluencingBounds(bounds);
                root.addChild(fogs[i]);
            }
        }

        // Set up the lights
        Light[] lights = scene.getLightNodes();
        if(lights != null) {
            if(ambient != null) {
                Light al = new AmbientLight(true, ambient);
                al.setInfluencingBounds(bounds);
                scene.addLightNode(al);
            }
            for(int i=0; i<lights.length; i++) {
                lights[i].setInfluencingBounds(bounds);
                root.addChild(lights[i]);
            }
        } else if(config.isDefaultLightingAllowed()) {
            // Set up default lighting
            debug("Using default lighting");
            Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
            Color3f light2Color = new Color3f(0.3f, 0.3f, 0.4f);
            Vector3f light2Direction  = new Vector3f(-6.0f, -2.0f, -1.0f);
            Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);

            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
            ambientLightNode.setInfluencingBounds(bounds);
            root.addChild(ambientLightNode);

            DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
            light1.setInfluencingBounds(bounds);
            root.addChild(light1);

            DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
            light2.setInfluencingBounds(bounds);
            root.addChild(light2);
        }

        TransformGroup trans = new TransformGroup();
        root.addChild(trans);

        // Add the objects
        for(int i=shapes.size()-1; i >= 0; i--) {
            trans.addChild((Node)shapes.get(i));
        }

        scene.setSceneGroup(root);
        scene.setScale(new Point3f(minx, miny, minz), new Point3f(maxx, maxy, maxz), limit);
        scene.setBounds(bounds);
        scene.setObjectParent(trans);
    }

// ******** Actual loader logic *********

    /**
     * Runs the processor on the specified file stream.
     */
    public void process() throws IncorrectFormatException, ParsingErrorException {
        ThreeDSBlock block = nextBlock();
        if(block.type != TYPE_3DS_FILE) {
            throw new IncorrectFormatException("Bad initial block type "+getTypeString(block.type)+".  Not a 3DS File.");
        }
        processFile(block);
        createScene();
    }

    protected void processFile(ThreeDSBlock root) {
        ThreeDSBlock block = nextBlock();
        if(block.type == TYPE_3DS_VERSION) {
            System.err.println("Loading 3DS File Version "+readShort());
            block = nextBlock(block);
        }
        while(block.type != TYPE_MESH_DATA) {
            debug("Unexpected block: "+getTypeString(block.type));
            block = nextBlock(block);
        }
        processMeshData(block);
        if(block.lastDataPosition < root.lastDataPosition) {
            block = nextBlock(block);
            while(block.type != TYPE_KEY_FRAME) {
                debug("Unexpected block: "+getTypeString(block.type)+" at "+block.start);
                block = nextBlock(block);
            }
            processKeyFrame(block);
        }
    }

    protected void processMeshData(ThreeDSBlock root) {
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_MESH_VERSION:
                    System.err.println("  Loading mesh data Version "+readShort());
                    break;
                case TYPE_MATERIAL:
                    processMaterial(block);
                    break;
                case TYPE_BACKGROUND_COLOR:
                    solidBackground = readColor();
                    break;
                case TYPE_BG_USE_SOLID:
                    if((config.getFlags() & config.LOAD_BACKGROUND_NODES) > 0) {
                        useSolidBackground = true;
                        debug("Background color: "+solidBackground);
                        if(solidBackground.x < 0.1f && solidBackground.y < 0.1f && solidBackground.z < 0.1f) {
                            System.err.println("Overriding dark background!");
                            solidBackground = null;
                        }
                    }
                    break;
                case TYPE_BG_BITMAP:
                    debug("Background bitmap: "+readString());
                    break;
                case TYPE_BG_USE_BITMAP:
                    System.err.println("Can't load background bitmaps yet.");
                    break;
                case TYPE_BG_GRADIENT:
                    float midpoint = readFloat();
                    Color3f start = readColor(), mid = readColor(), end = readColor();
                    debug("BG Gradient; midpoint="+midpoint);
                    debug("     Start: "+start);
                    debug("    Middle: "+mid);
                    debug("       End: "+end);
                    if(!useSolidBackground) {
                        solidBackground = mid;
                    }
                    break;
                case TYPE_BG_USE_GRADIENT:
                    System.err.println("Unable to display background gradient.  Using median as solid color instead.");
                    useSolidBackground = true;
                    break;
                case TYPE_AMBIENT_COLOR:
                    if((config.getFlags() & config.LOAD_LIGHT_NODES) > 0) {
                        ambient = readColor();
                        debug("Ambient color: "+ambient);
                    }
                    break;
                case TYPE_FOG:
                    if((config.getFlags() & Loader.LOAD_FOG_NODES) > 0) {
                        processFog(block);
                    }
                    break;
                case TYPE_LAYER_FOG:
                    if((config.getFlags() & Loader.LOAD_FOG_NODES) > 0) {
                        processLayerFog(block);
                    }
                    break;
                case TYPE_NAMED_OBJECT:
                    processObject(block);
                    break;
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
    }

    protected void processKeyFrame(ThreeDSBlock root) {
    }

    protected void ignoreMaterial(ThreeDSBlock root) {
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            if(block.type == TYPE_MATERIAL_NAME) {
                String name = readString();
                debug("Using default material for "+name);
                materials.put(name, defaultAppearance);
                return;
            }
        }
    }

    protected void processMaterial(ThreeDSBlock root) {
        Material material = new Material();
        material.setEmissiveColor(new Color3f(0.0f,0.0f,0.0f));
        Appearance appearance = new Appearance();
        appearance.setMaterial(material);
        if(config.isForceTwoSided()) {
            appearance.setPolygonAttributes(
                new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
                                      PolygonAttributes.CULL_NONE,
                                      0.0f,
                                      true
                )
            );
        }
        Color3f color;
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_MATERIAL_NAME:
                    String name = readString();
                    debug("Found material: "+name);
                    materials.put(name, appearance);
                    break;
                case TYPE_MAT_AMBIENT:
                    color = readColor();
                    debug("      Ambient: "+color);
                    material.setAmbientColor(color);
                    break;
                case TYPE_MAT_SPECULAR:
                    color = readColor();
                    debug("     Specular: "+color);
                    material.setSpecularColor(color);
                    break;
                case TYPE_MAT_DIFFUSE:
                    color = readColor();
                    debug("      Diffuse: "+color);
                    material.setDiffuseColor(color);
                    break;
                case TYPE_MAT_SHININESS:
                    float shin = 1.0f+127.0f*readPercentage();
                    debug("    Shininess: "+shin);
                    material.setShininess(shin);
                    break;
                case TYPE_MAT_SHININESS2:
                    float shin2 = 1.0f+127.0f*readPercentage();
                    debug("   Shininess2: "+shin2);
//                    material.setShininess(shin);
                    break;
                case TYPE_MAT_TRANSPARENCY:
                    float trans = readPercentage();
                    debug(" Transparency: "+trans);
                    if(trans == 0) {
                        appearance.setTransparencyAttributes(
                            new TransparencyAttributes(TransparencyAttributes.NONE, trans)
                          );
                    } else {
                        appearance.setTransparencyAttributes(
                            new TransparencyAttributes(TransparencyAttributes.NICEST, trans)
                          );
                    }
                    break;
                case TYPE_MAT_2_SIDED:
                    debug("  Detected 2-sided material");
                    appearance.setPolygonAttributes(
                        new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
                                              PolygonAttributes.CULL_NONE,
                                              0.0f,
                                              true
                        )
                    );
                    break;
                case TYPE_MAT_XPFALL:
                    float xpf = readPercentage();
                    debug("   XpfAll: "+xpf);
                    break;
                case TYPE_MAT_REFBLUR:
                    float ref = readPercentage();
                    debug("  RefBlur: "+ref);
                    break;
                case TYPE_MAT_SELF_ILPCT:
                    float il = readPercentage();
                    debug("SelfILPct: "+il);
                    break;
                case TYPE_MAT_SHADING:
                    short shading = readShort();
                    debug("  Shading: "+shading);
                    // Some kind of code for the rendering type: 1, 3, 4, etc.
                    break;
                case TYPE_MAT_TEXMAP:
                    debug("Texture Found: "+(readPercentage()*100)+"%");
                    processTexture(block);
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
    }

    protected void processTexture(ThreeDSBlock root) {
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_MAT_MAPNAME:
                    debug("Texture File Name: "+readString());
                    break;
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
    }

    protected void processObject(ThreeDSBlock root) {
        String name = readString();
        debug("Found Object "+name);
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_TRIANGLE_OBJECT:
                    Shape3D[] results = processTriangleObject(block);
                    if(results.length > 0) {
                        shapes.addAll(Arrays.asList(results));
                        scene.addNamedObject(name, results);
                    }
                    break;
                case TYPE_DIRECT_LIGHT:
                    if((config.getFlags() & config.LOAD_LIGHT_NODES) > 0) {
                        processLight(block, root.lastDataPosition);
                    }
                    break;
                case TYPE_CAMERA:
                    if((config.getFlags() & config.LOAD_VIEW_GROUPS) > 0) {
                        processCamera(block, root.lastDataPosition);
                    }
                    break;
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
    }

    protected Shape3D[] processTriangleObject(ThreeDSBlock root) {
        debug("  Object Type: Triangle Object");
        List shapes = new ArrayList();
        Point3f[] vertices = null;
        Point3f[][] faces;
        int count;
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_VERTEX_OPTIONS:
                    count = readShort();
                    for(int i=0; i<count; i++) {
                        short options = readShort();
                        // bits are significant for selection, etc.  Useless here.
                    }
                    break;
                case TYPE_POINT_LIST:
                    count = readShort();
                    vertices = new Point3f[count];
                    for(int i=0; i<count; i++) {
                        vertices[i] = readPoint();
                    }
                    debug("    Found "+count+" vertices");
                    break;
                case TYPE_FACE_LIST:
                    count = readShort();
                    if(count > 0) {
                        debug("    Found "+count+" faces");
                        faces = new Point3f[count][3];
                        for(int i=0; i<count; i++) {
                            faces[i][0] = vertices[readShort()];
                            faces[i][1] = vertices[readShort()];
                            faces[i][2] = vertices[readShort()];
                            readShort(); // Flags (?)
                        }
                        shapes.addAll(Arrays.asList(processFaceList(block, faces)));
                    }
                    break;
                case TYPE_MESH_MATRIX:
                    // Some type of offset for this object?
                    float matrix[][] = new float[4][3];
                    for(int i=0; i<matrix.length; i++) {
                        for(int j=0; j<matrix[i].length; j++) {
                            matrix[i][j] = readFloat();
                        }
                    }
                    break;
                case TYPE_MESH_COLOR:
                    byte color = readByte();
                    debug("Mesh Color: "+color);
                    // Is this something in the IDE?
                    break;

                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                break;
            } else {
                block = nextBlock(block);
            }
        }
        return (Shape3D[])shapes.toArray(new Shape3D[shapes.size()]);
    }

    protected Shape3D[] processFaceList(ThreeDSBlock root, Point3f[][] faces) {
        ThreeDSBlock block = nextBlock();
        List list = new ArrayList();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_MAT_FACE_LIST:
                    String name = readString();
                    int count = readShort();
                    debug("Found "+count+" faces for material "+name);
                    if(count > 0) {
                        Shape3D s3d = new Shape3D();
                        Appearance app = (Appearance)materials.get(name);
                        s3d.setAppearance(app);
                        GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
                        Point3f points[] = new Point3f[count*3];
                        for(int i=0; i<count; i++) {
                            int index = readShort();
                            points[i*3] = faces[index][0];
                            points[i*3+1] = faces[index][1];
                            points[i*3+2] = faces[index][2];
                        }
                        gi.setCoordinates(points);
                        new NormalGenerator().generateNormals(gi);
                        if(!config.isForceReverseNormals()) {
                            gi.reverse();
                            new NormalGenerator().generateNormals(gi);
                        }
                        s3d.setGeometry(gi.getIndexedGeometryArray(true));
                        list.add(s3d);
                    }
                    break;
                default:
                    unknownBlock(block);
                    break;
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                break;
            } else {
                block = nextBlock(block);
            }
        }
        return (Shape3D[])list.toArray(new Shape3D[list.size()]);
    }

    protected void processLight(ThreeDSBlock root, int lastDataPosition) {
        Light light = null;
        Point3f location = new Point3f(readFloat(), readFloat(), readFloat());
        Color3f color = readColor();
        Point3f attenuation = new Point3f(1f, 0f, 0f);
        debug("  Object Type: Light with color "+color+" and position "+location);

        if(position < lastDataPosition) {
            ThreeDSBlock block = nextBlock();
            while(block.lastDataPosition <= root.lastDataPosition) {
                switch(block.type) {
                    case TYPE_SPOTLIGHT:
                        Point3f target = new Point3f(readFloat(), readFloat(), readFloat());
                        Vector3f direction = new Vector3f(target.x - location.x, target.y-location.y, target.z-location.z);
                        float spotAngle = readFloat();
                        float falloff = readFloat();
                        debug("    Spotlight angle: "+spotAngle+", falloff: "+falloff+", target: "+target+", direction: "+direction);
                        light = new SpotLight(true, color, location, attenuation, direction, (float)Math.toRadians(falloff), (falloff-spotAngle)*128f/falloff);
                        break;
                    case TYPE_ATTENUATION:
                        debug("    Attenuation: "+block);
                        break;
                    default:
                        unknownBlock(block);
                }
                if(block.lastDataPosition == root.lastDataPosition) {
                    return;
                } else {
                    block = nextBlock(block);
                }
            }
        }
        if(light == null) {
            light = new PointLight(true, color, location, attenuation);
        }

        // FIXME: Do I need to set influencing bounds?

        scene.addLightNode(light);
    }

    protected void processCamera(ThreeDSBlock root, int lastDataPosition) {
        debug("  Object Type: Camera");
        Point3f camera = new Point3f(readFloat(), readFloat(), readFloat());
        Point3f target = new Point3f(readFloat(), readFloat(), readFloat());
        float bankAngle = readFloat();
        float focus = readFloat();
        if(position < lastDataPosition) {
            ThreeDSBlock block = nextBlock();
            while(block.lastDataPosition <= root.lastDataPosition) {
                switch(block.type) {
                    default:
                        unknownBlock(block);
                }
                if(block.lastDataPosition == root.lastDataPosition) {
                    return;
                } else {
                    block = nextBlock(block);
                }
            }
        }
    }

    protected void processFog(ThreeDSBlock root) {
        float nearPlane = readFloat();
        float nearDensity = readFloat();
        float farPlane = readFloat();
        float farDensity = readFloat();
        Color3f color = readColor();
        boolean use = false;
        debug("Found FOG: "+nearPlane+"/"+nearDensity+" - "+farPlane+"/"+farDensity);
        debug("    Color: "+color);
        if(position < root.lastDataPosition) {
            ThreeDSBlock block = nextBlock();
            while(block.lastDataPosition <= root.lastDataPosition) {
                switch(block.type) {
                    case TYPE_FOG_BGND:
                        debug(" FOG BGND");
                        break;
                    case TYPE_USE_FOG:
                        use = true;
                        break;
                    default:
                        unknownBlock(block);
                }
                if(block.lastDataPosition == root.lastDataPosition) {
                    return;
                } else {
                    block = nextBlock(block);
                }
            }
        }
        if(use) {
            Fog fog = new LinearFog(color, nearPlane, farPlane);
            scene.addFogNode(fog);
        }
    }

    protected void processLayerFog(ThreeDSBlock root) {
        float fogZFrom = readFloat();
        float fogZTo = readFloat();
        float fogDensity = readFloat();
        short fogType = readShort();
        debug("Found Layer Fog: "+fogZFrom+"-"+fogZTo+" Density: "+fogDensity+" Type: "+fogType);
        boolean use = false;
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                case TYPE_FOG_BGND:
                    debug(" FOG BGND");
                    break;
                case TYPE_USE_LAYER_FOG:
                    use = true;
                    break;
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
        if(use) {
            // What do we do with this?
        }
    }

/*
    protected void processTemplate(ThreeDSBlock root) {
        ThreeDSBlock block = nextBlock();
        while(block.lastDataPosition <= root.lastDataPosition) {
            switch(block.type) {
                default:
                    unknownBlock(block);
            }
            if(block.lastDataPosition == root.lastDataPosition) {
                return;
            } else {
                block = nextBlock(block);
            }
        }
    }

*/



// ******** Utility functions *********

    /**
     * Called for any unknown blocks.  Useful to try to locate a specific block
     * in the file if it's not where you think it is.
     */
    protected void unknownBlock(ThreeDSBlock block) {
        debug("*** UNKNOWN: "+getTypeString(block.type)+" @ "+Integer.toHexString(block.start));
    }

    /**
     * Reads a Point3f.  Adjusts the maximum bounds if appropriate.
     */
    protected Point3f readPoint() {
        float x = readFloat();
        float y = readFloat();
        float z = readFloat();
        if(x < minx) {
            minx = x;
        } else if(x > maxx) {
            maxx = x;
        }
        if(y < miny) {
            miny = y;
        } else if(y > maxy) {
            maxy = y;
        }
        if(z < minz) {
            minz = z;
        } else if(z > maxz) {
            maxz = z;
        }
        return new Point3f(x,y,z);
    }

    /**
     * Prints a debugging message, if debug output is enabled.
     */
    protected void debug(String message) {
        if(showDebugInfo) {
            System.out.println("DEBUG: "+message);
        }
    }

    /**
     * Skips the contents of a block.  Usually used for unrecognized blocks.
     */
    protected void skipBlock(ThreeDSBlock block) throws ParsingErrorException {
        int bytesToSkip = block.lastDataPosition + 1 - position;
        try {
            in.skipBytes(bytesToSkip);
            position += bytesToSkip;
        } catch(IOException e) {
            throw new ParsingErrorException("Unable to skip block: "+e);
        }
    }

    /**
     * Creates a new block from the current position in the stream.
     */
    protected ThreeDSBlock nextBlock() throws ParsingErrorException {
        int start = position;
        short type = readShort();
        int length = readInt();
        if(length < 0) {
            throw new ParsingErrorException("Bad block read: length="+length);
        }
        ThreeDSBlock block = new ThreeDSBlock(type, start, length);
        return block;
    }

    /**
     * Skips any remaining content in the parameter block, and then
     * creates a new block from the next position in the stream.
     */
    protected ThreeDSBlock nextBlock(ThreeDSBlock previous) throws ParsingErrorException {
        skipBlock(previous);
        ThreeDSBlock block = nextBlock();
        return block;
    }

    /**
     * Reads a color from the input file.
     */
    protected Color3f readColor() throws ParsingErrorException {
        ThreeDSBlock block = nextBlock();
        switch(block.type) {
            case TYPE_COLOR_F:
            case TYPE_COLOR_LIN_F:
                return new Color3f(readFloat(),readFloat(),readFloat());
            case TYPE_COLOR_I:
            case TYPE_COLOR_LIN_I:
                return new Color3f(readUnsignedByte()/255.0f,readUnsignedByte()/255.0f,readUnsignedByte()/255.0f);
            default:
                throw new ParsingErrorException("Invalid color type "+getTypeString(block.type));
        }
    }

    /**
     * Reads a percentage value from the input file.  Returns as a float
     * between 0 and 1.
     */
    protected float readPercentage() throws ParsingErrorException {
        ThreeDSBlock block = nextBlock();
        if(block.type == TYPE_PERCENT_I) {
            return (float)readShort() / 100.0f;
        } else if(block.type == TYPE_PERCENT_F) {
            return readFloat();
        } else {
            throw new ParsingErrorException("Invalid percentage type "+block.type);
        }
    }

    /**
     * Reads a String value from the input file.
     */
    protected String readString() throws ParsingErrorException {
        try {
            StringBuffer sb = new StringBuffer("");
            ++position;
            byte ch = in.readByte();
            while(ch != (byte)0) {
                sb.append((char)ch);
                ++position;
                ch = in.readByte();
            }
            return sb.toString();
        }catch(IOException e) {
            throw new ParsingErrorException("Unable to read String: "+e);
        }
    }

    /**
     * Reads a float value from the imput file.
     */
    protected float readFloat() throws ParsingErrorException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a byte (8-bit) value from the input file.
     */
    protected byte readByte() throws ParsingErrorException {
        try {
            ++position;
            return in.readByte();
        } catch(IOException e) {
            throw new ParsingErrorException("Unable to read byte: "+e);
        }
    }

    /**
     * Reads an unsigned byte (8-bit) value from the input file.
     */
    protected int readUnsignedByte() throws ParsingErrorException {
        try {
            ++position;
            return in.read();
        } catch(IOException e) {
            throw new ParsingErrorException("Unable to read unsigned byte: "+e);
        }
    }

    /**
     * Reads a short (16-bit) value from the input file.
     */
    protected short readShort() throws ParsingErrorException {
        try {
            position += 2;
            return (short)(in.read()+(in.read() << 8));
        }catch(IOException e) {
            throw new ParsingErrorException("Unable to read short: "+e);
        }
    }

    /**
     * Reads an int (32-bit) value from the input file.
     */
    protected int readInt() throws ParsingErrorException {
        try {
            position += 4;
            return (int)(in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24));
        }catch(IOException e) {
            throw new ParsingErrorException("Unable to read int: "+e);
        }
    }

    /**
     * Gets a type as a Hex string.
     */
    protected static String getTypeString(short type) {
        String temp = Integer.toHexString(type);
        int length = temp.length();
        if(length > 4) {
            return temp.substring(length-4, length);
        } else {
            return temp;
        }
    }
}