package programming.project;

import java.util.HashMap;
import java.util.Map;
import acm.graphics.GPoint;

public class PuzzleModel {
	private static GPoint[][] boardGrid;
	private static Map<String, GPoint[]> piecePositions = new HashMap<String, GPoint[]>();
	GamePiece topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	public PuzzleModel() {
		boardGrid = new GPoint[4][6];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				boardGrid[i][j] = new GPoint(i, j);
			}
		}

		topLeft = new GamePiece(new GPoint(0, 0), new GPoint(0, 1), new GPoint(1, 0), "topLeft");
		topLeft.setCurrentPosition(new GPoint(0, 0));
		piecePositions.put(topLeft.getName(), topLeft.getCoordinates());

		topRight = new GamePiece(new GPoint(2, 0), new GPoint(3, 0), new GPoint(3, 1), "topRight");
		topRight.setCurrentPosition(new GPoint(2, 0));
		piecePositions.put(topRight.getName(), topRight.getCoordinates());

		bottomLeft = new GamePiece(new GPoint(0, 2), new GPoint(0, 3), new GPoint(1, 3), "bottomLeft");
		bottomLeft.setCurrentPosition(new GPoint(0, 2));
		piecePositions.put(bottomLeft.getName(), bottomLeft.getCoordinates());

		bottomRight = new GamePiece(new GPoint(2, 3), new GPoint(3, 2), new GPoint(3, 3), "bottomRight");
		bottomRight.setCurrentPosition(new GPoint(2, 2));
		piecePositions.put(bottomRight.getName(), bottomRight.getCoordinates());

		middleSquare = new GamePiece(new GPoint(1, 1), new GPoint(1, 2), new GPoint(2, 1), new GPoint(2, 2),
				"middleSquare");
		middleSquare.setCurrentPosition(new GPoint(1, 1));
		piecePositions.put(middleSquare.getName(), middleSquare.getCoordinates());

	}

	public Map<String, GPoint[]> getPiecePositions() {
		return piecePositions;
	}

	public void setPiecePositions(String name, GPoint[] newPosition) {
		piecePositions.put(name, newPosition);
//		for (Map.Entry<String, GPoint[]> entry : this.getPiecePositions().entrySet()) {
//			for (int i = 0; i < entry.getValue().length; i++) {
//				System.out.println(" " + entry.getKey() + " " + entry.getValue()[i]);
//			}
//		}
	}

	public boolean checkIntersection(GamePiece activePiece) {
		GPoint[] activePieceCoordinates = activePiece.getCoordinates();
		for (Map.Entry<String, GPoint[]> entry : this.getPiecePositions().entrySet()) {
			GPoint[] temp = entry.getValue();
			for (int i = 0; i < temp.length; i++) {
//				System.out.println(entry.getKey() + " " + temp[i]);
				for (int j = 0; j < activePieceCoordinates.length; j++) {
					if (!entry.getKey().equals(activePiece.getName()) && temp[i].equals(activePieceCoordinates[j])) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
