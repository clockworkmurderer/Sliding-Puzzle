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
	private Color inactivePieceColor = new Color(0, 51, 153);
	private Color activePieceColor = new Color(51, 153, 102);

	private GCompound topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	private String name = new String();
	private GPoint lastClick;
	private GamePiece activePiece;

	private boolean playing = false;

	@Override
	public void init() {
		super.init();
		addMouseListeners();
		addKeyListeners();
	}

	@Override
	public void run() {
		introduction();
	}

	/** Introduce the player to the game. */
	public void introduction() {
		setSize(300, 100);
		GLabel welcome = new GLabel("Welcome to the Schimmler sliding puzzle!");
		GLabel welcome2 = new GLabel("Click the screen to advance the instructions.");
		add(welcome, 25, 25);
		add(welcome2, 24, 40);
		waitForClick();
		removeAll();

		GLabel directions1 = new GLabel("To play, click the piece you would like to");
		GLabel directions2 = new GLabel("move and move it with the WASD keys.");
		welcome = new GLabel("Press N at any time to reset the puzzle.");
		add(directions1, 25, 25);
		add(directions2, 25, 40);
		add(welcome, 25, 55);
		waitForClick();
		removeAll();

		welcome = new GLabel("A piece can only be moved into adjacent");
		directions1 = new GLabel("empty cells at 90 degree angles. In order to win,");
		directions2 = new GLabel("you will need to move the pieces until they match");
		welcome2 = new GLabel("the following configuration.");
		add(welcome, 40, 25);
		add(directions1, 20, 40);
		add(directions2, 20, 55);
		add(welcome2, 65, 70);
		waitForClick();
		removeAll();

		view.showSolution();
		waitForClick();
		removeAll();

		setSize(300, 100);
		welcome = new GLabel("Good luck!");
		welcome2 = new GLabel("Click to begin the game.");
		add(welcome, 110, 50);
		add(welcome2, 75, 75);
		waitForClick();
		removeAll();
		playing = true;
		gameStart();
	}

	/** Start the game. */
	public void gameStart() {
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
		if (playing) {
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
		} else {
			return;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (playing) {
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
				case 'N':
					reset();
					break;
				default:
					break;
				}
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
		if (board.winCondition()) {
			victory();
		}
	}

	/** Resets the puzzle. */
	public void reset() {
		board = new PuzzleModel();
		moveCounter = 0;
		repaintBoard();
	}

	/** Displays the victory message upon completing the puzzle. */
	public void victory() {
		pause(8000);
		removeAll();
		setSize(300, 100);
		GLabel victory = new GLabel("You completed the puzzle in only " + moveCounter + " moves!");
		add(victory, 100, 75);
		// System.out.println("You completed the puzzle in only " + moveCounter + "
		// moves!");
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

		private void showSolution() {
			removeAll();
			gridSetup();
			add(drawTopLeft(inactivePieceColor), 0, 0);
			add(drawTopRight(inactivePieceColor), 2 * CELL_SIZE, 0);
			add(drawBottomLeft(inactivePieceColor), 0, 2 * CELL_SIZE);
			add(drawBottomRight(inactivePieceColor), 2 * CELL_SIZE, 2 * CELL_SIZE);
			add(drawMiddleSquare(inactivePieceColor), 1 * CELL_SIZE, 4 * CELL_SIZE);
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

			bottomRight = new GCompound();

			GRect bottomPiece = new GRect(GAP, CELL_SIZE + GAP, CELL_SIZE * 2 - GAP * 2, CELL_SIZE - GAP * 2);
			bottomPiece.setColor(c);
			bottomPiece.setFilled(true);
			bottomRight.add(bottomPiece);

			GRect sidePiece = new GRect(CELL_SIZE + GAP, GAP, CELL_SIZE - GAP * 2, CELL_SIZE * 2 - GAP * 2);
			sidePiece.setFilled(true);
			sidePiece.setColor(c);
			bottomRight.add(sidePiece);

			return bottomRight;
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