package programming.project;

import acm.graphics.GPoint;

public class GamePiece {
	private String name;
	private GPoint currentPosition;
	private GPoint[] coordinates;

	public GamePiece(GPoint a, GPoint b, GPoint c, String name) {
		coordinates = new GPoint[3];
		coordinates[0] = a;
		coordinates[1] = b;
		coordinates[2] = c;
		this.name = name;
	}

	public GamePiece(GPoint a, GPoint b, GPoint c, GPoint d, String name) {
		coordinates = new GPoint[4];
		coordinates[0] = a;
		coordinates[1] = b;
		coordinates[2] = c;
		coordinates[3] = d;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public GPoint[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GPoint[] pieceCoordinates) {
		coordinates = pieceCoordinates;
	}

	public GPoint getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(GPoint shapeOrigin) {
		currentPosition = shapeOrigin;
	}

	public void movePiece(int x, int y) {
		for (GPoint p : this.coordinates) {
			p.setLocation(p.getX() + x, p.getY() + y);
		}
		this.setCurrentPosition(new GPoint(this.getCurrentPosition().getX() + x, this.getCurrentPosition().getY() + y));
	}
}
