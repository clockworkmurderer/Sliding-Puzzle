package programming.project;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import acm.graphics.GCompound;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class PuzzleController extends GraphicsProgram {

	private static final int CELL_SIZE = 80;
	private static final int GAP = 8;

	private PuzzleModel board = new PuzzleModel();
	private PuzzleView view = new PuzzleView();
	Color inactivePieceColor = new Color(0, 51, 153);
	Color activePieceColor = new Color(51, 153, 102);

	static GCompound topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	String name = new String();
	private GPoint lastClick;
	private GamePiece activePiece;

	@Override
	public void init() {
		super.init();
		setSize(CELL_SIZE * 4 + 10, CELL_SIZE * 6 + 10);
		addMouseListeners();
		addKeyListeners();
	}

	public void run() {
		repaintBoard();
	}

	public void repaintBoard() {
		removeAll();
		view.gridSetup();
		view.draw(board.topLeft, inactivePieceColor);
		view.draw(board.topRight, inactivePieceColor);
		view.draw(board.bottomLeft, inactivePieceColor);
		view.draw(board.bottomRight, inactivePieceColor);
		view.draw(board.middleSquare, inactivePieceColor);
	}

	public void mousePressed(MouseEvent e) {
		activePiece = null;
		repaintBoard();
		lastClick = new GPoint(e.getPoint());
		lastClick.setLocation((int) lastClick.getX() / CELL_SIZE, (int) lastClick.getY() / CELL_SIZE);

		for (Map.Entry<String, GPoint[]> entry : board.getPiecePositions().entrySet()) {
			GPoint[] temp = entry.getValue();
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals(lastClick)) {
					name = entry.getKey();
				}
			}
		}

		if (name != null) {
			switch (name) {
			case "topLeft":
				activePiece = board.topLeft;
				view.draw(activePiece, activePieceColor);
				break;
			case "topRight":
				activePiece = board.topRight;
				view.draw(activePiece, activePieceColor);
				break;
			case "bottomLeft":
				activePiece = board.bottomLeft;
				view.draw(activePiece, activePieceColor);
				break;
			case "bottomRight":
				activePiece = board.bottomRight;
				view.draw(activePiece, activePieceColor);
				break;
			case "middleSquare":
				activePiece = board.middleSquare;
				view.draw(activePiece, activePieceColor);
				break;
			default:
				break;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		if (activePiece != null) {
			switch (Character.toUpperCase(e.getKeyChar())) {
			case 'W':
				movement(0, -1);
				break;
			case 'S':
				movement(0, 1);
				break;
			case 'A':
				movement(-1, 0);
				break;
			case 'D':
				movement(1, 0);
				break;
			default:
				break;
			}
		}
	}

	private void movement(int x, int y) {
		GPoint[] destination = activePiece.getCoordinates();
		for (int i = 0; i < destination.length; i++) {
			destination[i] = new GPoint(activePiece.getCoordinates()[i].getX() + x,
					activePiece.getCoordinates()[i].getY() + y);
		}
		if (board.checkIntersection(new GamePiece(destination, activePiece.getName()))) {
			return;
		} else {
			activePiece.movePiece(x, y);
			board.setPiecePositions(activePiece.getName(), activePiece.getCoordinates());
			repaintBoard();
			view.draw(activePiece, activePieceColor);
		}
	}

	class PuzzleView {
		public void draw(GamePiece piece, Color c) {

			if (piece.equals(board.topLeft)) {
				add(drawTopLeft(c), piece.getCurrentPosition().getX() * CELL_SIZE,
						piece.getCurrentPosition().getY() * CELL_SIZE);
			} else if (piece.equals(board.topRight)) {
				add(drawTopRight(c), piece.getCurrentPosition().getX() * CELL_SIZE,
						piece.getCurrentPosition().getY() * CELL_SIZE);
			} else if (piece.equals(board.bottomLeft)) {
				add(drawBottomLeft(c), piece.getCurrentPosition().getX() * CELL_SIZE,
						piece.getCurrentPosition().getY() * CELL_SIZE);
			} else if (piece.equals(board.bottomRight)) {
				add(drawBottomRight(c), piece.getCurrentPosition().getX() * CELL_SIZE,
						piece.getCurrentPosition().getY() * CELL_SIZE);
			} else if (piece.equals(board.middleSquare)) {
				add(drawMiddleSquare(c), piece.getCurrentPosition().getX() * CELL_SIZE,
						piece.getCurrentPosition().getY() * CELL_SIZE);
			} else {
			}
		}

		private void gridSetup() {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 6; j++) {
					GRect cell = new GRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					add(cell);
				}
			}
		}

		private GCompound drawTopLeft(Color c) {

			topLeft = new GCompound();

			GRect topPiece = new GRect(GAP, GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE - GAP * 2);
			topPiece.setColor(c);
			topPiece.setFilled(true);
			topLeft.add(topPiece);

			GRect sidePiece = new GRect(GAP, GAP, CELL_SIZE - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			sidePiece.setFilled(true);
			sidePiece.setColor(c);
			topLeft.add(sidePiece);

			return topLeft;
		}

		private GCompound drawTopRight(Color c) {

			topRight = new GCompound();

			GRect topPiece = new GRect(GAP, GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE - GAP * 2);
			topPiece.setColor(c);
			topPiece.setFilled(true);
			topRight.add(topPiece);

			GRect sidePiece = new GRect(CELL_SIZE + GAP, GAP, CELL_SIZE - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			sidePiece.setFilled(true);
			sidePiece.setColor(c);
			topRight.add(sidePiece);

			return topRight;
		}

		private GCompound drawBottomLeft(Color c) {

			bottomLeft = new GCompound();

			GRect bottomPiece = new GRect(GAP, CELL_SIZE + GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE - GAP * 2);
			bottomPiece.setColor(c);
			bottomPiece.setFilled(true);
			bottomLeft.add(bottomPiece);

			GRect sidePiece = new GRect(GAP, GAP, CELL_SIZE - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			sidePiece.setFilled(true);
			sidePiece.setColor(c);
			bottomLeft.add(sidePiece);

			return bottomLeft;
		}

		private GCompound drawBottomRight(Color c) {

			bottomLeft = new GCompound();

			GRect bottomPiece = new GRect(GAP, CELL_SIZE + GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE - GAP * 2);
			bottomPiece.setColor(c);
			bottomPiece.setFilled(true);
			bottomLeft.add(bottomPiece);

			GRect sidePiece = new GRect(CELL_SIZE + GAP, GAP, CELL_SIZE - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			sidePiece.setFilled(true);
			sidePiece.setColor(c);
			bottomLeft.add(sidePiece);

			return bottomLeft;
		}

		private GCompound drawMiddleSquare(Color c) {

			middleSquare = new GCompound();

			GRect middle = new GRect(GAP, GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			middle.setFilled(true);
			middle.setColor(c);
			middleSquare.add(middle);

			return middleSquare;
		}
	}
}