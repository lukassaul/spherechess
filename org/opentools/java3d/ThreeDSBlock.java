package org.opentools.java3d;

/**
 * A block in a 3DS file.  A block has a type and length, plus content, which
 * is composed of other blocks and data values.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.0 $
 */
public class ThreeDSBlock {
    public final static int BLOCK_HEADER_SIZE = 6;
    public short type;
    public int start;
    public int dataStart;
    public int length;
    public int dataLength;
    public int lastDataPosition;

    /**
     * Creates a new block with the specified type, start position in the
     * file, and length (in bytes).
     */
    public ThreeDSBlock(short type, int start, int length) {
        this.type = type;
        this.length = length;
        this.start = start;
        dataStart = start + BLOCK_HEADER_SIZE;
        dataLength = length - BLOCK_HEADER_SIZE;
        lastDataPosition = start + length - 1;
    }

    /**
     * Returns a description of this block.
     */
    public String toString() {
        return "Block "+ThreeDSProcessor.getTypeString(type)+" Pos "+Integer.toHexString(start)+"-"+Integer.toHexString(lastDataPosition)+" Data "+Integer.toHexString(dataStart)+" length "+Integer.toHexString(length)+"/"+Integer.toHexString(dataLength);
    }
}