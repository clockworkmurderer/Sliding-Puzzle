package programming.project;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import de.cau.infprogoo.lighthouse.LighthouseDisplay;

/**
 * This class controls the sliding puzzle. Its internal View class handles the
 * graphics.
 */
public class PuzzleController extends GraphicsProgram {

	private static final int CELL_SIZE = 80;
	private static final int GAP = 8;
	private static final int CELL_WIDTH = 9;
	private static final int CELL_HEIGHT = 168;
	private static final int OFFSET = 108;

	private PuzzleModel board = new PuzzleModel();
	private PuzzleView view = new PuzzleView();
	private PuzzleLightHouseView lightHouseView = new PuzzleLightHouseView();

	private RandomGenerator rgen = new RandomGenerator();

	private GPoint lastClick;
	private GLabel numberOfMoves;
	private GCompound topLeft, topRight, bottomLeft, bottomRight, middleSquare;

	private GamePiece activePiece;
	private String name = new String();

	private Color inactivePieceColor = new Color(0, 51, 153);
	private Color activePieceColor = new Color(51, 153, 102);

	private boolean playing;

	private int moveCounter = 0;

	@Override
	public void init() {
		super.init();
		addMouseListeners();
		addKeyListeners();
	}

	@Override
	public void run() {
		introduction();
		playing = true;
		gameStart();
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
		if (!board.winCondition()) {
			removeAll();
			view.gridSetup();
			view.draw(board.topLeft, inactivePieceColor);
			view.draw(board.topRight, inactivePieceColor);
			view.draw(board.bottomLeft, inactivePieceColor);
			view.draw(board.bottomRight, inactivePieceColor);
			view.draw(board.middleSquare, inactivePieceColor);
		} else {
			removeAll();
			view.gridSetup();
			view.draw(board.topLeft, Color.GREEN);
			view.draw(board.topRight, Color.GREEN);
			view.draw(board.bottomLeft, Color.GREEN);
			view.draw(board.bottomRight, Color.GREEN);
			view.draw(board.middleSquare, Color.GREEN);
		}
		lightHouseView.draw();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (playing) {
			repaintBoard();
			lastClick = new GPoint(e.getPoint());
			checkClick();
			nameCheck();
		} else
			return;
	}

	/**
	 * Checks against the HashMap to see if the clicked cell is occupied by a piece.
	 */
	private void checkClick() {
		lastClick.setLocation((int) lastClick.getX() / CELL_SIZE, (int) lastClick.getY() / CELL_SIZE);
		for (Map.Entry<String, GPoint[]> entry : board.getPiecePositions().entrySet()) {
			GPoint[] temp = entry.getValue();
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals(lastClick)) {
					name = entry.getKey();
				}
			}
		}
	}

	/**
	 * Uses the key from the HashMap to figure out which, if any, piece was clicked
	 * and then sets the active piece to the piece which was clicked.
	 */
	private void nameCheck() {
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
		if (playing && !board.winCondition()) {
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
		} else if (playing && board.winCondition()) {
			lightHouseView.victory();
			victory();
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

	/** Resets the puzzle. */
	public void reset() {
		board = new PuzzleModel();
		moveCounter = 0;
		repaintBoard();
	}

	/** Displays the victory message upon completing the puzzle. */
	public void victory() {
		pause(3000);
		removeAll();
		setSize(300, 100);
		GLabel victory = new GLabel("You completed the puzzle in only " + moveCounter + " moves!");
		add(victory, 25, 75);
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
			if (piece == board.topLeft) {
				add(drawTopLeft(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece == board.topRight) {
				add(drawTopRight(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece == board.bottomLeft) {
				add(drawBottomLeft(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece == board.bottomRight) {
				add(drawBottomRight(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
			} else if (piece == board.middleSquare) {
				add(drawMiddleSquare(c), piece.getOrigin().getX() * CELL_SIZE, piece.getOrigin().getY() * CELL_SIZE);
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

		/** Used during the instructions to show the user what the objective is. */
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

	/**
	 * The PuzzleView is responsible for showing the user what is happening. It
	 * accomplishes this by providing the draw method and the graphical version of
	 * the game board and pieces. Additionally, this view can be shown on the
	 * university skyscraper.
	 */
	class PuzzleLightHouseView {

		LighthouseDisplay display = new LighthouseDisplay("stu213278", "API-TOK_mZqc-DQ4t-6bau-6ZmT-MxIK");
		private byte[] data = new byte[14 * 28 * 3];

		/**
		 * This method draws the pieces and sends them to the lighthouse screen.
		 */
		public void draw() {
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) 0;
			}
			connect(display);
			fillArray();
			sendArray(display);
		}

		/** This method draws the border. */
		private void drawBorder() {
			for (int offset = 21; offset <= 1113; offset += 84) {
				data[offset] = (byte) 255;
				data[offset + 1] = (byte) 255;
				data[offset + 2] = (byte) 255;
			}
			for (int offset = 60; offset <= 1152; offset += 84) {
				data[offset] = (byte) 255;
				data[offset + 1] = (byte) 255;
				data[offset + 2] = (byte) 255;
			}
			for (int offset = 1116; offset <= 1151; offset++) {
				data[offset] = (byte) 255;
			}
			for (int offset = 24; offset <= 59; offset++) {
				data[offset] = (byte) 255;
			}
		}

		/** This method draws the game board. */
		private void fillArray() {
			drawBorder();
			for (Map.Entry<String, GPoint[]> entry : board.getPiecePositions().entrySet()) {
				switch (entry.getKey()) {
				case "topLeft":
					drawPiece(board.topLeft, 15, 200, 150);
					break;
				case "topRight":
					drawPiece(board.topRight, 5, 255, 5);
					break;
				case "bottomLeft":
					drawPiece(board.bottomLeft, 255, 10, 10);
					break;
				case "bottomRight":
					drawPiece(board.bottomRight, 200, 0, 200);
					break;
				case "middleSquare":
					drawPiece(board.middleSquare, 255, 255, 0);
				}
			}
		}

		/**
		 * This method takes three ints and a GamePiece as arguments and draws the piece
		 * based on whether it is active or not.
		 */
		private void drawPiece(GamePiece pieceToBeDrawn, int red, int green, int blue) {
			if (activePiece == pieceToBeDrawn) {
				for (int i = 0; i < pieceToBeDrawn.getCoordinates().length; i++) {
					drawCell((int) (pieceToBeDrawn.getCoordinates()[i].getX() * CELL_WIDTH
							+ pieceToBeDrawn.getCoordinates()[i].getY() * CELL_HEIGHT), 255, 255, 255);
				}
			} else {
				for (int i = 0; i < pieceToBeDrawn.getCoordinates().length; i++) {
					drawCell((int) (pieceToBeDrawn.getCoordinates()[i].getX() * CELL_WIDTH
							+ pieceToBeDrawn.getCoordinates()[i].getY() * CELL_HEIGHT), red, green, blue);
				}
			}
		}

		/** This method draws the windows of a single cell on the game board. */
		private void drawCell(int index, int red, int green, int blue) {
			index += OFFSET;
			drawWindows(index, red, green, blue);
			drawWindows(index + 3, red, green, blue);
			drawWindows(index + 6, red, green, blue);
			drawWindows(index + 84, red, green, blue);
			drawWindows(index + 87, red, green, blue);
			drawWindows(index + 90, red, green, blue);
		}

		/** This method draws a single window. */
		private void drawWindows(int index, int red, int green, int blue) {
			data[index] = (byte) red;
			data[index + 1] = (byte) green;
			data[index + 2] = (byte) blue;
		}

		/** This method sends the byte array to the lighthouse. */
		private void sendArray(LighthouseDisplay display) {
			try {
				display.send(data);
				try {
					Thread.sleep(100);
				} catch (Exception couldNotSleep) {
					couldNotSleep.printStackTrace();
					System.exit(-1);
				}
			} catch (IOException wellAtLeastYouTried) {
				System.out.println("Connection failed: " + wellAtLeastYouTried.getMessage());
				wellAtLeastYouTried.printStackTrace();
			}
		}

		/** This method tries to connect to the lighthouse. */
		private void connect(LighthouseDisplay display) {
			try { // Try connecting to the display
				display.connect();
			} catch (Exception failedToConnect) {
				System.out.println("Connection failed: " + failedToConnect.getMessage());
				failedToConnect.printStackTrace();
			}
		}

		/** This method makes the windows flash in a dazzling victory dance. */
		public void victory() {
			boolean done = false;
			int count = 0;
			do {
				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) rgen.nextInt(0, 255);
				}
				sendArray(display);
				try {
					Thread.sleep(500);
					count++;
				} catch (Exception couldNotSleep) {
					couldNotSleep.printStackTrace();
					System.exit(-1);
				}
				if (count == 10) {
					done = true;
				}
			} while (!done);

			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) 0;
			}
			sendArray(display);
		}
	}
}