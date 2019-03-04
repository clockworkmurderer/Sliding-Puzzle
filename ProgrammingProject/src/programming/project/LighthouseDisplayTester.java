package programming.project;

import java.io.IOException;
import de.cau.infprogoo.lighthouse.LighthouseDisplay;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Map;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

/**
 * This class controls the sliding puzzle. Its internal View class handles the
 * graphics.
 */
public class LighthouseDisplayTester extends GraphicsProgram {

	private PuzzleModel board = new PuzzleModel();
	private PuzzleView view = new PuzzleView();
	private Color inactivePieceColor = new Color(0, 51, 153);
	private Color activePieceColor = new Color(51, 153, 102);

	private String name = new String();
	private GamePiece activePiece;

	@Override
	public void init() {
		super.init();
		addKeyListeners();
	}

	@Override
	public void run() {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!board.winCondition()) {
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
			} else {
				switch (Character.toUpperCase(e.getKeyChar())) {
				case 'R':
					nextPiece();
					break;
				case 'N':
					reset();
					break;
				default:
					break;
				}
			}
		} else if (board.winCondition()) {
			victory();
		}
	}

	/** This method sets the active piece to the next piece on the board. */
	private void nextPiece() {
		if (activePiece == board.topLeft)
			activePiece = board.topRight;
		else if (activePiece == board.topRight)
			activePiece = board.bottomLeft;
		else if (activePiece == board.bottomLeft)
			activePiece = board.bottomRight;
		else if (activePiece == board.bottomRight)
			activePiece = board.middleSquare;
		else if (activePiece == board.middleSquare)
			activePiece = board.topLeft;
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
	}

	public void victory() {
		pause(3000);
	}

	/**
	 * The PuzzleView is responsible for showing the user what is happening. It
	 * accomplishes this by providing the draw method and the graphical version of
	 * the game board and pieces.
	 */
	class PuzzleView {

		LighthouseDisplay display = new LighthouseDisplay("stu213278", "API-TOK_QUqH-fE+a-+oJN-UItd-pt/Q");
		private byte[] data = new byte[14 * 28 * 3];

		/**
		 * This method takes a GamePiece as an argument and draws it with the specified
		 * color.
		 */
		public void draw() {
			connect(display);
			fillArray();
			sendArray(display);
		}

		private void sendArray(LighthouseDisplay display) {
			try {
				while (true) {
					display.send(data);
					try {
						Thread.sleep(1000);
					} catch (Exception couldNotSleep) {
						couldNotSleep.printStackTrace();
						System.exit(-1);
					}
				}
			} catch (IOException wellAtLeastYouTried) {
				System.out.println("Connection failed: " + wellAtLeastYouTried.getMessage());
				wellAtLeastYouTried.printStackTrace();
			}
		}

		private void fillArray() {
		}

		private void connect(LighthouseDisplay display) {
			try { // Try connecting to the display
				display.connect();
			} catch (Exception failedToConnect) {
				System.out.println("Connection failed: " + failedToConnect.getMessage());
				failedToConnect.printStackTrace();
			}
		}
	}
}
