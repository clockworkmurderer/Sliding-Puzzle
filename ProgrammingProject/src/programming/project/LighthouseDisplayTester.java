package programming.project;

import java.io.IOException;
import de.cau.infprogoo.lighthouse.LighthouseDisplay;
import java.awt.event.KeyEvent;
import java.util.Map;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * This class controls the sliding puzzle. Its internal View class handles the
 * graphics.
 */
public class LighthouseDisplayTester extends GraphicsProgram {

	RandomGenerator rgen = new RandomGenerator();
	private PuzzleModel board = new PuzzleModel();
	private PuzzleView view = new PuzzleView();
	private static final int CELL_WIDTH = 9;
	private static final int CELL_HEIGHT = 168;
	private static final int OFFSET = 108;

	private GamePiece activePiece;

	boolean moving;

	@Override
	public void init() {
		super.init();
		addKeyListeners();
	}

	@Override
	public void run() {
		view.draw();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!board.winCondition()) {
			if (activePiece == null) {
				activePiece = board.topLeft;
			} else {
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
				case '1':
					activePiece = board.topLeft;
					System.out.println(activePiece.getName());
					view.draw();
					break;
				case '2':
					activePiece = board.topRight;
					System.out.println(activePiece.getName());
					view.draw();
					break;
				case '3':
					activePiece = board.bottomLeft;
					System.out.println(activePiece.getName());
					view.draw();
					break;
				case '4':
					activePiece = board.bottomRight;
					System.out.println(activePiece.getName());
					view.draw();
					break;
				case '5':
					activePiece = board.middleSquare;
					System.out.println(activePiece.getName());
					view.draw();
					break;
				default:
					break;
				}
			}
		} else if (board.winCondition()) {
			view.victory();
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
			activePiece.movePiece(x, y);
			System.out.println("Test");
			board.setPiecePositions(activePiece.getName(), activePiece.getCoordinates());
			view.draw();
		}
	}

	/** Resets the puzzle. */
	public void reset() {
		board = new PuzzleModel();
		view.draw();
	}

	/**
	 * The PuzzleView is responsible for showing the user what is happening. It
	 * accomplishes this by providing the draw method and the graphical version of
	 * the game board and pieces.
	 */
	class PuzzleView {

		LighthouseDisplay display = new LighthouseDisplay("stu213278", "API-TOK_mZqc-DQ4t-6bau-6ZmT-MxIK");
		private byte[] data = new byte[14 * 28 * 3];

		/**
		 * This method takes a GamePiece as an argument and draws it with the specified
		 * color.
		 */
		public void draw() {
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) 0;
			}
			connect(display);
			fillArray();
			sendArray(display);
		}

		private void fillArray() {
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

		private void drawPiece(GamePiece pieceToBeDrawn, int red, int green, int blue) {
			drawBorder();
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

		private void drawCell(int index, int red, int green, int blue) {
			index += OFFSET;
			drawWindows(index, red, green, blue);
			drawWindows(index + 3, red, green, blue);
			drawWindows(index + 6, red, green, blue);
			drawWindows(index + 84, red, green, blue);
			drawWindows(index + 87, red, green, blue);
			drawWindows(index + 90, red, green, blue);
		}

		private void drawWindows(int index, int red, int green, int blue) {
			data[index] = (byte) red;
			data[index + 1] = (byte) green;
			data[index + 2] = (byte) blue;
		}

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

		private void connect(LighthouseDisplay display) {
			try { // Try connecting to the display
				display.connect();
			} catch (Exception failedToConnect) {
				System.out.println("Connection failed: " + failedToConnect.getMessage());
				failedToConnect.printStackTrace();
			}
		}

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
