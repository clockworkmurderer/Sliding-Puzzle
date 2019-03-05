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
	private static final int CELL_WIDTH = 9;
	private static final int CELL_HEIGHT = 168;

	private GamePiece activePiece;

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

		LighthouseDisplay display = new LighthouseDisplay("stu213278", "API-TOK_CXzd-U3Qd-pTSH-agBO-yoTj");
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

		private void fillArray() {

			for (Map.Entry<String, GPoint[]> entry : board.getPiecePositions().entrySet()) {
				for (int thisLine = 0; thisLine < 6; thisLine++) {
					GPoint[] temp = entry.getValue();
					int red = 0;
					int green = 0;
					int blue = 0;
					int index = 0;
					switch (entry.getKey()) {
					case "topLeft":
						red = 26;
						green = 158;
						blue = 200;
						index = 0;
						for (int i = 0; i < temp.length; i++) {
							if ((int) temp[i].getY() == thisLine) {
								index = (int) board.topLeft.getOrigin().getX();
								drawCell(index + (i * CELL_WIDTH), red, green, blue);
							} else if (temp[i].getY() == thisLine + 1) {
								index = (int) board.topLeft.getOrigin().getX();
								drawCell(index + CELL_HEIGHT, red, green, blue);
							}
						}
						break;
					case "topRight":
//						red = 144;
//						green = 179;
//						blue = 100;
//						index = 0;
//						for (int i = 0; i < temp.length; i++) {
//							if ((int) temp[i].getY() == thisLine) {
//								index = (int) board.topRight.getOrigin().getX() * CELL_WIDTH * 2;
//								drawCell(index, red, green, blue);
//								drawCell(index + CELL_WIDTH, red, green, blue);
//							} else if (temp[i].getY() == thisLine + 1) {
//								drawCell(index + CELL_HEIGHT + CELL_WIDTH, red, green, blue);
//							}
//						}
						break;
					case "bottomLeft":
//						red = 26;
//						green = 158;
//						blue = 200;
//						index = 0;
//						for (int i = 0; i < temp.length; i++) {
//							if ((int) temp[i].getY() == thisLine) {
//								index = (int) temp[i].getX();
//								drawCell(index, red, green, blue);
//								drawCell(index + CELL_WIDTH, red, green, blue);
//							} else {
//								drawCell(index + CELL_HEIGHT, red, green, blue);
//							}
//						}
						break;
					case "bottomRight":
//						red = 26;
//						green = 158;
//						blue = 200;
//						index = 0;
//						for (int i = 0; i < temp.length; i++) {
//							if ((int) temp[i].getY() == thisLine) {
//								index = (int) temp[i].getX();
//								drawCell(index, red, green, blue);
//								drawCell(index + CELL_WIDTH, red, green, blue);
//							} else {
//								drawCell(index + CELL_HEIGHT, red, green, blue);
//							}
//						}
						break;
					case "middleSquare":
//						red = 255;
//						green = 255;
//						blue = 255;
//						index = 0;
//						for (int i = 0; i < temp.length; i++) {
//							if ((int) temp[i].getY() == thisLine) {
//								index = (int) temp[i].getX();
//								drawCell(index, red, green, blue);
//								drawCell(index + CELL_WIDTH, red, green, blue);
//							} else {
//								drawCell(index + CELL_HEIGHT, red, green, blue);
//							}
//						}
						break;
					default:
						break;
					}
				}
			}
		}

		private void drawCell(int index, int red, int green, int blue) {
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
//			data[index + 3] = (byte) 0;
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
