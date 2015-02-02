package org.opentools.java3d;

/**
 * Constants representing block types in the 3DS file.  This is not a complete
 * list; it only includes the types that are currently recognized.  They are
 * all of type <code>short</code> since 3DS files allocate 16 bits to the
 * block type field.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public interface ThreeDSConstants {
    // 0xMISC Administrative codes
    public static final short TYPE_3DS_FILE         = (short)0x4D4D;
    public static final short TYPE_3DS_VERSION      = (short)0x0002;
    public static final short TYPE_MESH_DATA        = (short)0x3D3D;
    public static final short TYPE_MESH_VERSION     = (short)0x3D3E;

    // 0x0--- Data type codes
    public static final short TYPE_COLOR_I          = (short)0x0011;
    public static final short TYPE_COLOR_F          = (short)0x0010;
    public static final short TYPE_COLOR_LIN_I      = (short)0x0012;
    public static final short TYPE_COLOR_LIN_F      = (short)0x0013;
    public static final short TYPE_PERCENT_I        = (short)0x0030;
    public static final short TYPE_PERCENT_F        = (short)0x0031;

    // 0x1--- Background codes
    public static final short TYPE_BG_BITMAP        = (short)0x1100;
    public static final short TYPE_BG_USE_BITMAP    = (short)0x1101;
    public static final short TYPE_BACKGROUND_COLOR = (short)0x1200;
    public static final short TYPE_BG_USE_SOLID     = (short)0x1201;
    public static final short TYPE_BG_GRADIENT      = (short)0x1300;
    public static final short TYPE_BG_USE_GRADIENT  = (short)0x1301;

    // 0x2--- Ambient light/fog code
    public static final short TYPE_AMBIENT_COLOR    = (short)0x2100;
    public static final short TYPE_FOG              = (short)0x2200;
    public static final short TYPE_USE_FOG          = (short)0x2201;
    public static final short TYPE_FOG_BGND         = (short)0x2210;
    public static final short TYPE_LAYER_FOG        = (short)0x2302;
    public static final short TYPE_USE_LAYER_FOG    = (short)0x2302;

    // 0x3--- View codes

    // 0x4--- Object data codes
    public static final short TYPE_NAMED_OBJECT     = (short)0x4000;
    public static final short TYPE_TRIANGLE_OBJECT  = (short)0x4100;
    public static final short TYPE_POINT_LIST       = (short)0x4110;
    public static final short TYPE_VERTEX_OPTIONS   = (short)0x4111;
    public static final short TYPE_FACE_LIST        = (short)0x4120;
    public static final short TYPE_MAT_FACE_LIST    = (short)0x4130;
    public static final short TYPE_SMOOTH_GROUP     = (short)0x4150;
    public static final short TYPE_MESH_MATRIX      = (short)0x4160;
    public static final short TYPE_MESH_COLOR       = (short)0x4165;
    // 0x46-- Light data codes
    public static final short TYPE_DIRECT_LIGHT     = (short)0x4600;
    public static final short TYPE_SPOTLIGHT        = (short)0x4610;
    public static final short TYPE_ATTENUATION      = (short)0x4625;
    public static final short TYPE_AMBIENT_LIGHT    = (short)0x4680;
    // 0x47-- Camera data codes
    public static final short TYPE_CAMERA           = (short)0x4700;

    // 0x5--- Unknown shape stuff

    // 0x6--- Path/curve codes

    // 0x7--- Viewport codes

    // 0x8--- Unknown XDATA codes

    // 0xA--- Material codes
    public static final short TYPE_MATERIAL         = (short)0xAFFF;
    public static final short TYPE_MATERIAL_NAME    = (short)0xA000;
    public static final short TYPE_MAT_AMBIENT      = (short)0xA010;
    public static final short TYPE_MAT_DIFFUSE      = (short)0xA020;
    public static final short TYPE_MAT_SPECULAR     = (short)0xA030;
    public static final short TYPE_MAT_SHININESS    = (short)0xA040;
    public static final short TYPE_MAT_SHININESS2   = (short)0xA041;
    public static final short TYPE_MAT_TRANSPARENCY = (short)0xA050;
    public static final short TYPE_MAT_XPFALL       = (short)0xA052;
    public static final short TYPE_MAT_REFBLUR      = (short)0xA053;
    public static final short TYPE_MAT_2_SIDED      = (short)0xA081;
    public static final short TYPE_MAT_SELF_ILPCT   = (short)0xA084;
    public static final short TYPE_MAT_SHADING      = (short)0xA100;
    // 0xA2+- Texture codes
    public static final short TYPE_MAT_TEXMAP       = (short)0xA200;
    public static final short TYPE_MAT_MAPNAME      = (short)0xA300;

    // 0xB--- Node codes
    public static final short TYPE_KEY_FRAME        = (short)0xB000;


    // 0xC--- Unknown codes
    // 0xD--- Unknown codes
    // 0xF--- Unknown codes
}