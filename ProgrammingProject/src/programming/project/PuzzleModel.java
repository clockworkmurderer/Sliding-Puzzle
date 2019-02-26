package programming.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import acm.graphics.GPoint;

/**
 * This class represents a sliding puzzle game board. The locations of the
 * pieces and the methods required to move them are included in this class.
 */
public class PuzzleModel {
	/**
	 * These HashMaps contain the positions of the pieces on the game board and the
	 * positions required to win the game.
	 */
	private static Map<String, GPoint[]> piecePositions = new HashMap<String, GPoint[]>();

	/** The pieces. */
	GamePiece topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	/**
	 * This constructor initializes the game pieces, sets their locations to the
	 * start position, and initializes the HashMaps.
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

		middleSquare = new GamePiece(new GPoint(0, 4), new GPoint(0, 5), new GPoint(1, 4), new GPoint(1, 5),
				"middleSquare");
		middleSquare.setOrigin(new GPoint(0, 4));
		piecePositions.put(middleSquare.getName(), middleSquare.getCoordinates());

	}

	/** This method checks the win condition of the puzzle. */
	public boolean winCondition() {

		boolean victory1, victory2, victory3, victory4, victory5;
		GPoint[] pieceCoords;

		pieceCoords = new GPoint[] { new GPoint(0, 0), new GPoint(0, 1), new GPoint(1, 0) };
		if (Arrays.equals(getPiecePositions().get("topLeft"), pieceCoords)) {
			victory1 = true;
		} else {
			victory1 = false;
		}

		pieceCoords = new GPoint[] { new GPoint(2, 0), new GPoint(3, 0), new GPoint(3, 1) };
		if (Arrays.equals(getPiecePositions().get("topRight"), pieceCoords)) {
			victory2 = true;
		} else {
			victory2 = false;
		}

		pieceCoords = new GPoint[] { new GPoint(0, 2), new GPoint(0, 3), new GPoint(1, 3) };
		if (Arrays.equals(getPiecePositions().get("bottomLeft"), pieceCoords)) {
			victory3 = true;
		} else {
			victory3 = false;
		}

		pieceCoords = new GPoint[] { new GPoint(2, 3), new GPoint(3, 2), new GPoint(3, 3) };
		if (Arrays.equals(getPiecePositions().get("bottomRight"), pieceCoords)) {
			victory4 = true;
		} else {
			victory4 = false;
		}

		pieceCoords = new GPoint[] { new GPoint(1, 4), new GPoint(1, 5), new GPoint(2, 4), new GPoint(2, 5) };
		if (Arrays.equals(getPiecePositions().get("middleSquare"), pieceCoords)) {
			victory5 = true;
		} else {
			victory5 = false;
		}

		if (victory1 && victory2 && victory3 && victory4 && victory5) {
			return true;
		} else {
			return false;
		}

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
					 * This if statement returns false if the move would result in any part of the
					 * piece overlapping another piece or laying outside of the puzzle grid. The
					 * first two boolean checks rule out the piece "colliding" with itself.
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
