public class Move {

	private String moveString;

	private Square startSquare;
	private Square finishSquare;

	private BoardConfig startConfig;
	private BoardConfig finishConfig;

	private Piece piece;

	public Move (String s) {
		moveString = s;
	}

	public Move(Square s, Square f, Piece p) {
		startSquare = s;
		finishSquare = f;
		piece = p;
	}

	public Piece getPiece () { return piece; }

}