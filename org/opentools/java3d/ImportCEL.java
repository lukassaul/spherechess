package org.opentools.java3d;

import java.io.*;

/**
 * Started work on loading .CEL files, which is a format 3DS can use to
 * store textures.  Made it all the way up to decoding the compressed
 * geometry.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class ImportCEL implements Runnable {
    public int height, width;
    public int size;
    public int aspectx, aspecty;

    private File input;

    public ImportCEL() {
    }

    public void setFile(File file) {
        input = file;
    }

    public void run() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(input));
            size = in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24);
            short type = (short)(in.read() + (in.read() << 8));
            if(type != (short)0xAF12) {
                throw new RuntimeException("Not a CEL file: "+Integer.toHexString(type));
            }
            short frames = (short)(in.read() + (in.read() << 8));
            if(frames != (short)1) {
                throw new RuntimeException("Can't handle an animation!");
            }
            width = in.read() + (in.read() << 8);
            height = in.read() + (in.read() << 8);
            int depth = in.read() + (in.read() << 8);
            if(depth != (short)8) {
                throw new RuntimeException("Unknown BPP: "+depth);
            }
            for(int i=0; i<24; i++) {
                in.read();
            }
            aspectx = in.read() + (in.read() << 8);
            aspecty = in.read() + (in.read() << 8);
            for(int i=0; i<38; i++) {
                in.read();
            }
            int offset1 = in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24);
            int offset2 = in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24);
            for(int i=0; i<40; i++) {
                in.read();
            }
            while(true) {
                int length = in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24);
                type = (short)(in.read() + (in.read() << 8));
                System.out.println("Found chunk: "+Integer.toHexString(type)+" "+length+" bytes.");
                if(type == (short)0xf1fa && length > width) {
                    short subs = (short)(in.read() + (in.read() << 8));
                    for(int i=0; i<8; i++) {
                        in.read();
                    }
                    int max = (int)subs;
                    for(int i=0; i<max; i++) {
                        int sublen = in.read() + (in.read() << 8) + (in.read() << 16) + (in.read() << 24);
                        int subtype = (short)(in.read() + (in.read() << 8));
                        System.out.println("Found Sub: "+Integer.toHexString(subtype)+" "+sublen+" bytes.");
                        for(int j=6; j<sublen; j++) {
                            in.read();
                        }
                    }
                } else {
                    for(int i=6; i<length; i++) {
                        in.read();
                    }
                }
                if(in.available() == 0) {
                    return;
                }
            }

        } catch(IOException e) {e.printStackTrace();}
    }

    public String toString() {
        return "Texture "+input.getName()+"\n"+
               "    Size: "+width+" x "+height+"\n"+
               "  Aspect: "+aspectx+":"+aspecty+"\n"+
               "   Bytes: "+size;
    }

    public static void main(String args[]) {
        ImportCEL importer = new ImportCEL();
        importer.setFile(new File(args[0]));
        importer.run();
        System.out.println(importer);
    }
}