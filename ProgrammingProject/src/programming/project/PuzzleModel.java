package programming.project;

import java.util.HashMap;
import java.util.Map;
import acm.graphics.GPoint;

/**
 * This class represents a sliding puzzle game board. The locations of the
 * pieces and the methods required to move them are included in this class.
 */
public class PuzzleModel {
	/** This HashMap contains the positions of the pieces on the game board. */
	private static Map<String, GPoint[]> piecePositions = new HashMap<String, GPoint[]>();
	/** The pieces. */
	GamePiece topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	/**
	 * This constructor initializes the game pieces, sets their locations to the
	 * start position, and puts the pieces into the HashMap.
	 */
	public PuzzleModel() {

		topLeft = new GamePiece(new GPoint(0, 0), new GPoint(0, 1), new GPoint(1, 0), "topLeft");
		topLeft.setOrigin(new GPoint(0, 0));
		piecePositions.put(topLeft.getName(), topLeft.getCoordinates());

		topRight = new GamePiece(new GPoint(2, 0), new GPoint(3, 0), new GPoint(3, 1), "topRight");
		topRight.setOrigin(new GPoint(2, 0));
		piecePositions.put(topRight.getName(), topRight.getCoordinates());

		bottomLeft = new GamePiece(new GPoint(0, 2), new GPoint(0, 3), new GPoint(1, 3), "bottomLeft");
		bottomLeft.setOrigin(new GPoint(0, 2));
		piecePositions.put(bottomLeft.getName(), bottomLeft.getCoordinates());

		bottomRight = new GamePiece(new GPoint(2, 3), new GPoint(3, 2), new GPoint(3, 3), "bottomRight");
		bottomRight.setOrigin(new GPoint(2, 2));
		piecePositions.put(bottomRight.getName(), bottomRight.getCoordinates());

		middleSquare = new GamePiece(new GPoint(1, 1), new GPoint(1, 2), new GPoint(2, 1), new GPoint(2, 2),
				"middleSquare");
		middleSquare.setOrigin(new GPoint(1, 1));
		piecePositions.put(middleSquare.getName(), middleSquare.getCoordinates());

	}

	/**
	 * Returns the positions of the pieces on the board.
	 * 
	 * @return The positions of the pieces on the board
	 */
	public Map<String, GPoint[]> getPiecePositions() {
		return piecePositions;
	}

	/** Sets the positions of the pieces on the board. */
	public void setPiecePositions(String name, GPoint[] newPosition) {
		piecePositions.put(name, newPosition);
	}

	/**
	 * This method checks to see whether a move is valid.
	 * 
	 * @activePiece The active piece.
	 */
	public boolean isMoveValid(GamePiece activePiece) {
		GPoint[] activePieceCoordinates = activePiece.getCoordinates();
		// the outermost layer of this for loop traverses the entries of the HashMap
		for (Map.Entry<String, GPoint[]> entry : this.getPiecePositions().entrySet()) {
			GPoint[] temp = entry.getValue(); // retrieve the coordinates of the given entry

			/**
			 * This part of the loop is nested in order to avoid any
			 * ArrayOutOfBoundsExceptions. Since the middle square takes up four cells and
			 * the other pieces only take up three, I needed two loop control variables.
			 */
			for (int i = 0; i < temp.length; i++) {
				for (int j = 0; j < activePieceCoordinates.length; j++) {
					/**
					 * This heavy if statement returns false if the move would result in any part of
					 * the piece overlapping another piece or laying outside of the puzzle grid.
					 */
					if (!entry.getKey().equals(activePiece.getName()) && temp[i].equals(activePieceCoordinates[j])
							|| ((int) activePieceCoordinates[j].getX()) < 0
							|| ((int) activePieceCoordinates[j].getX()) > 3
							|| ((int) activePieceCoordinates[j].getY()) < 0
							|| ((int) activePieceCoordinates[j].getY()) > 5) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
