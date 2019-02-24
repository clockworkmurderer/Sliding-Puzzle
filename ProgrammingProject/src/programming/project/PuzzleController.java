package programming.project;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

/**
 * This class controls the sliding puzzle. Its internal View class handles the
 * graphics.
 */
public class PuzzleController extends GraphicsProgram {

	private static final int CELL_SIZE = 80;
	private static final int GAP = 8;

	private int moveCounter = 0;
	private GLabel numberOfMoves;

	private PuzzleModel board = new PuzzleModel();
	private PuzzleView view = new PuzzleView();
	Color inactivePieceColor = new Color(0, 51, 153);
	Color activePieceColor = new Color(51, 153, 102);

	GCompound topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	String name = new String();
	private GPoint lastClick;
	private GamePiece activePiece;

	@Override
	public void init() {
		super.init();
		addMouseListeners();
		addKeyListeners();
	}

	@Override
	public void run() {
		repaintBoard();
	}

	/** This method repaints the board. */
	public void repaintBoard() {
		removeAll();
		view.gridSetup();
		view.draw(board.topLeft, inactivePieceColor);
		view.draw(board.topRight, inactivePieceColor);
		view.draw(board.bottomLeft, inactivePieceColor);
		view.draw(board.bottomRight, inactivePieceColor);
		view.draw(board.middleSquare, inactivePieceColor);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		activePiece = null;
		repaintBoard();
		lastClick = new GPoint(e.getPoint());

		/**
		 * The next line reduces the clicked location to a fraction of the CELL_SIZE in
		 * order to conform to the coordinate system.
		 */
		lastClick.setLocation((int) lastClick.getX() / CELL_SIZE, (int) lastClick.getY() / CELL_SIZE);

		/**
		 * Checks against the HashMap to see if the clicked cell is occupied by a piece.
		 */
		for (Map.Entry<String, GPoint[]> entry : board.getPiecePositions().entrySet()) {
			GPoint[] temp = entry.getValue();
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals(lastClick)) {
					name = entry.getKey();
				}
			}
		}

		/**
		 * Uses the key from the HashMap to figure out which, if any, piece was clicked
		 * and then sets the active piece to the piece which was clicked.
		 */
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

	@Override
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

	/**
	 * This method determines the destination cells of the given move for the given
	 * piece and passes that information to the Model in order to check if the move
	 * being made is valid.
	 */
	private void movement(int x, int y) {
		GPoint[] destination = new GPoint[activePiece.getCoordinates().length];
		for (int i = 0; i < destination.length; i++) {
			destination[i] = new GPoint(activePiece.getCoordinates()[i].getX() + x,
					activePiece.getCoordinates()[i].getY() + y);
		}

		// if the move is not valid, do nothing. otherwise, make the move
		if (!board.isMoveValid(new GamePiece(destination, activePiece.getName()))) {
			return;
		} else {
			moveCounter++;
			activePiece.movePiece(x, y);
			board.setPiecePositions(activePiece.getName(), activePiece.getCoordinates());
			repaintBoard();
			view.draw(activePiece, activePieceColor);
		}
	}

	/**
	 * The PuzzleView is responsible for showing the user what is happening. It
	 * accomplishes this by providing the draw method and the graphical version of
	 * the game board and pieces.
	 */
	class PuzzleView {
		/**
		 * This method takes a GamePiece as an argument and draws it with the specified
		 * color.
		 */
		public void draw(GamePiece piece, Color c) {

			if (piece.equals(board.topLeft)) {
				add(drawTopLeft(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece.equals(board.topRight)) {
				add(drawTopRight(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece.equals(board.bottomLeft)) {
				add(drawBottomLeft(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece.equals(board.bottomRight)) {
				add(drawBottomRight(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece.equals(board.middleSquare)) {
				add(drawMiddleSquare(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else {
			}
		}

		/** This method sets up the game board and the move counter label. */
		private void gridSetup() {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 6; j++) {
					GRect cell = new GRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					add(cell);
				}
			}
			setSize(CELL_SIZE * 4 + 10, CELL_SIZE * 7 + 10);
			numberOfMoves = new GLabel("Number of Moves: " + moveCounter, CELL_SIZE, CELL_SIZE * 6 + 25);
			add(numberOfMoves);
		}

		/** Draws the top left piece. */
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

		/** Draws the top right piece. */
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

		/** Draws the bottom left piece. */
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

		/** Draws the bottom right piece. */
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

		/** Draws the middle piece. */
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