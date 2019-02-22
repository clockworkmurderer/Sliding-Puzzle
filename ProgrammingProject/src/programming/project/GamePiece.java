package programming.project;

import acm.graphics.GPoint;

public class GamePiece {
	private String name;
	private GPoint currentPosition;
	private GPoint[] coordinates;

	public GamePiece(GPoint a, GPoint b, GPoint c, String name) {
		this.coordinates = new GPoint[3];
		this.coordinates[0] = a;
		this.coordinates[1] = b;
		this.coordinates[2] = c;
		this.name = name;
	}

	public GamePiece(GPoint a, GPoint b, GPoint c, GPoint d, String name) {
		this.coordinates = new GPoint[4];
		this.coordinates[0] = a;
		this.coordinates[1] = b;
		this.coordinates[2] = c;
		this.coordinates[3] = d;
		this.name = name;
	}

	public GamePiece(GPoint[] coords, String name) {
		this.coordinates = coords;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public GPoint[] getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(GPoint[] pieceCoordinates) {
		this.coordinates = pieceCoordinates;
	}

	public GPoint getCurrentPosition() {
		return this.currentPosition;
	}

	public void setCurrentPosition(GPoint shapeOrigin) {
		this.currentPosition = shapeOrigin;
	}

	public void movePiece(int x, int y) {
		for (GPoint p : this.coordinates) {
			p.setLocation(p.getX() + x, p.getY() + y);
		}
		this.setCurrentPosition(new GPoint(this.getCurrentPosition().getX() + x, this.getCurrentPosition().getY() + y));
	}
}
