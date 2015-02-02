

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
*  The Configuration
*  What pieces and where they are on the board.
*
*  This class looks to be heavy with chess rule stuff.
*
*  How many of these are there?
*/
public class BoardConfig {

	private Piece[] pieces;

	public BoardConfig() {
	}

	public boolean isMate() {
		return false;
	}

	public boolean isCheck() {
		return false;
	}

	public boolean isStalemate() {
		return false;
	}

	public void load(FileInputStream fis) {

	}

	public void load(file f) {
	}

	public void save(FileOutputStream fos) {

	}

	public void save(file f) {
	}

}