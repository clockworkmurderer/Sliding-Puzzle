package programming.project;

import acm.graphics.GPoint;

/**
 * Instances of this class represents pieces of a sliding puzzle. Each piece
 * consists of a name and the coordinates of the cells it occupies.
 */
public class GamePiece {

	/** The name of the piece. */
	private String name;
	/** The origin of the piece. */
	private GPoint origin;
	/** The coordinates of the cells the piece occupies. */
	private GPoint[] coordinates;

	/** Constructor for a GamePiece which occupies three cells. */
	public GamePiece(GPoint a, GPoint b, GPoint c, String name) {
		this.coordinates = new GPoint[3];
		this.coordinates[0] = a;
		this.coordinates[1] = b;
		this.coordinates[2] = c;
		this.name = name;
	}

	/** Constructor for a GamePiece which occupies four cells. */
	public GamePiece(GPoint a, GPoint b, GPoint c, GPoint d, String name) {
		this.coordinates = new GPoint[4];
		this.coordinates[0] = a;
		this.coordinates[1] = b;
		this.coordinates[2] = c;
		this.coordinates[3] = d;
		this.name = name;
	}

	/**
	 * Constructor for a GamePiece using a GPoint[] as an argument to provide the
	 * cell coordinates.
	 */
	public GamePiece(GPoint[] coords, String name) {
		this.coordinates = coords;
		this.name = name;
	}

	/**
	 * Returns the name of the piece.
	 * 
	 * @return The name of the piece.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the coordinates of the piece.
	 * 
	 * @return The coordinates of the piece.
	 */
	public GPoint[] getCoordinates() {
		return this.coordinates;
	}

	/** Sets the coordinates of the piece. */
	public void setCoordinates(GPoint[] pieceCoordinates) {
		this.coordinates = pieceCoordinates;
	}

	/**
	 * Returns the origin of the piece.
	 * 
	 * @return The origin of the piece.
	 */
	public GPoint getOrigin() {
		return this.origin;
	}

	/** Sets the origin of the piece. */
	public void setOrigin(GPoint shapeOrigin) {
		this.origin = shapeOrigin;
	}

	/** This method changes the coordinates of a piece in order to move it. */
	public void movePiece(int x, int y) {
		for (GPoint p : this.coordinates) {
			p.setLocation(p.getX() + x, p.getY() + y);
		}
		this.setOrigin(new GPoint(this.getOrigin().getX() + x, this.getOrigin().getY() + y));
	}
}
